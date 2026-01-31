package com.api.ewallet.service.api.impl;

import com.api.ewallet.configuration.properties.WalletConfigProperties;
import com.api.ewallet.enums.ActiveStatus;
import com.api.ewallet.enums.InvalidExceptionEnum;
import com.api.ewallet.enums.KycStatus;
import com.api.ewallet.enums.TransactionStatus;
import com.api.ewallet.exception.*;
import com.api.ewallet.model.entity.Transaction;
import com.api.ewallet.model.entity.User;
import com.api.ewallet.model.entity.Wallet;
import com.api.ewallet.model.external.ExternalUserResponse;
import com.api.ewallet.model.wallet.SendMoneyRequest;
import com.api.ewallet.model.wallet.SendMoneyResponse;
import com.api.ewallet.repository.TransactionRepository;
import com.api.ewallet.repository.UserRepository;
import com.api.ewallet.repository.WalletRepository;
import com.api.ewallet.repository.specification.BaseSpecification;
import com.api.ewallet.repository.specification.TransactionSpecification;
import com.api.ewallet.repository.specification.UserSpecification;
import com.api.ewallet.service.api.SendMoneyService;
import com.api.ewallet.service.ws.ExternalUserService;
import com.api.ewallet.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SendMoneyServiceImpl implements SendMoneyService {

    private final WalletConfigProperties walletConfigProperties;
    private final ExternalUserService externalUserService;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional(noRollbackFor = TransactionFailedException.class)
    public SendMoneyResponse initiateSendMoney(String userId, SendMoneyRequest request) {
        log.info("Initiating send money for userId: {}", userId);

        User senderUser = getActiveSender(userId);
        User recipientUser = getActiveRecipient(request.getUsername(), request.getPhoneNumber());

        if (senderUser.getId().equals(recipientUser.getId())) {
            log.error("Invalid transaction. User {} attempted to send money to their own wallet.", userId);
            throw new InvalidTransactionException("Invalid transaction. Unable to send to own wallet.");
        }

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(InvalidExceptionEnum.WALLET.getCode()));
        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            log.error("Insufficient balance. Available: {}, Required: {}", wallet.getBalance(), request.getAmount());
            throw new InsufficientBalanceException("Insufficient balance");
        }

        checkDailyLimit(senderUser.getUserId(), request.getAmount());

        Transaction transaction = createPendingTransaction(senderUser, recipientUser, request);
        transactionRepository.saveAndFlush(transaction);

        try {
            ExternalUserResponse sender = externalUserService.getByUserId(senderUser.getUserId());
            ExternalUserResponse recipient = externalUserService.getByUserId(recipientUser.getUserId());

            performTransfer(senderUser.getUserId(), recipientUser.getUserId(), request.getAmount(), transaction);

            return buildSendMoneyResponse(transaction, sender, recipient);
        } catch (Exception ex) {
            log.error("Transaction {} failed: {}", transaction.getTransactionId(), ex.getMessage());

            updateTransactionStatus(transaction, TransactionStatus.FAILED.getCode());
            throw new TransactionFailedException("Transaction failed");
        }
    }

    private User getActiveSender(String userId) {
        User user = userRepository.findOne(UserSpecification.byUserId(userId)
                        .and(BaseSpecification.isActive()))
                .orElseThrow(() -> new NotFoundException(InvalidExceptionEnum.USER.getCode()));

        if (!Objects.equals(user.getKycStatus(), KycStatus.VERIFIED.getCode())) {
            log.error("Invalid transaction. User {} is not verified yet.", userId);
            throw new InvalidTransactionException("Invalid transaction. User is not verified yet.");
        }
        return user;
    }

    private User getActiveRecipient(String username, String phoneNumber) {
        User user = userRepository.findOne(UserSpecification.byUsername(username)
                        .and(UserSpecification.byPhoneNumber(phoneNumber))
                        .and(BaseSpecification.isActive()))
                .orElseThrow(() -> new NotFoundException(InvalidExceptionEnum.RECIPIENT.getCode()));

        if (!Objects.equals(user.getKycStatus(), KycStatus.VERIFIED.getCode())) {
            log.error("Invalid transaction. Recipient {} is not verified yet.", user.getUserId());
            throw new InvalidTransactionException("Invalid transaction. Recipient is not verified yet.");
        }
        return user;
    }

    private void checkDailyLimit(String userId, BigDecimal amount) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        BigDecimal dailyLimit = walletConfigProperties.getDailyLimit();

        Specification<Transaction> spec = TransactionSpecification.bySenderUserId(userId)
                .and(TransactionSpecification.byTransactionStatus(TransactionStatus.COMPLETED.getCode()))
                .and(TransactionSpecification.byCreatedAtBetween(startOfDay, endOfDay)
                        .and(BaseSpecification.isActive()));

        BigDecimal totalSentToday = transactionRepository.findAll(spec).stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal newTotal = totalSentToday.add(amount);

        log.debug("Daily transfer check - Current: {}, Requested: {}, New Total: {}, Limit: {}",
                totalSentToday, amount, newTotal, dailyLimit);

        if (newTotal.compareTo(dailyLimit) > 0) {
            throw new DailyLimitExceededException("Exceeded daily transfer limit");
        }
    }

    private Transaction createPendingTransaction(User sender, User recipient, SendMoneyRequest request) {
        return Transaction.builder()
                .transactionId(IdUtil.generateTransactionId())
                .senderUserId(sender.getUserId())
                .recipientUserId(recipient.getUserId())
                .referenceId(IdUtil.generateReferenceId())
                .amount(request.getAmount())
                .currency("PHP")
                .transactionStatus(TransactionStatus.PENDING.getCode())
                .status(ActiveStatus.ACTIVE.getCode())
                .note(request.getNote())
                .build();
    }

    private void performTransfer(String senderUserId, String recipientUserId, BigDecimal amount, Transaction transaction) {
        deductBalance(senderUserId, amount);
        creditBalance(recipientUserId, amount);

        transaction.setTransactionStatus(TransactionStatus.COMPLETED.getCode());
        transactionRepository.save(transaction);

        log.info("Transaction {} completed successfully", transaction.getTransactionId());
    }

    private void deductBalance(String userId, BigDecimal amount) {
        log.debug("Deducting {} from userId: {}", amount, userId);
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(InvalidExceptionEnum.WALLET.getCode()));

        BigDecimal newBalance = wallet.getBalance().subtract(amount);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        log.debug("New balance for sender {}: {}", userId, newBalance);
    }

    private void creditBalance(String userId, BigDecimal amount) {
        log.debug("Crediting {} to userId: {}", amount, userId);
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(InvalidExceptionEnum.WALLET.getCode()));

        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        log.debug("New balance for recipient {}: {}", userId, newBalance);
    }

    private SendMoneyResponse buildSendMoneyResponse(
            Transaction transaction,
            ExternalUserResponse sender,
            ExternalUserResponse recipient) {
        return SendMoneyResponse.builder()
                .transactionId(transaction.getTransactionId())
                .referenceId(transaction.getReferenceId())
                .status(TransactionStatus.getDescription(transaction.getTransactionStatus()))
                .timeStamp(LocalDateTime.now())
                .build();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateTransactionStatus(Transaction transaction, String status) {
        transaction.setTransactionStatus(status);
        transactionRepository.save(transaction);
    }
}
