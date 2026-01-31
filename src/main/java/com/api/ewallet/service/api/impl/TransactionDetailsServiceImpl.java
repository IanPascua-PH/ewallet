package com.api.ewallet.service.api.impl;

import com.api.ewallet.enums.InvalidExceptionEnum;
import com.api.ewallet.enums.TransactionStatus;
import com.api.ewallet.exception.NotFoundException;
import com.api.ewallet.model.entity.Transaction;
import com.api.ewallet.model.entity.User;
import com.api.ewallet.model.external.ExternalUserResponse;
import com.api.ewallet.model.wallet.TransactionResponse;
import com.api.ewallet.repository.TransactionRepository;
import com.api.ewallet.repository.UserRepository;
import com.api.ewallet.repository.specification.BaseSpecification;
import com.api.ewallet.repository.specification.TransactionSpecification;
import com.api.ewallet.repository.specification.UserSpecification;
import com.api.ewallet.service.api.TransactionDetailsService;
import com.api.ewallet.service.ws.ExternalUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionDetailsServiceImpl implements TransactionDetailsService {

    private final ExternalUserService externalUserService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    private static final String TRANSFER_FUNDS = "Transfer Funds";
    private static final String RECEIVE_FUNDS = "Receive Funds";

    @Override
    public TransactionResponse getTransactionDetails(String userId, String transactionId){
        log.info("Fetching transaction details for transactionId: {} by user: {}", transactionId, userId);

        User user = userRepository.findOne(UserSpecification.byUserId(userId)
                        .and(BaseSpecification.isActive()))
                .orElseThrow(() -> new NotFoundException(InvalidExceptionEnum.USER.getCode()));

        Transaction transaction = transactionRepository.findOne(TransactionSpecification.bySenderUserId(userId)
                        .or(TransactionSpecification.byRecipientUserId(userId))
                        .and(BaseSpecification.isActive()))
                .orElseThrow(() -> new NotFoundException(InvalidExceptionEnum.TRANSACTION.getCode()));

        String description = transaction.getSenderUserId().equals(user.getUserId()) ? TRANSFER_FUNDS : RECEIVE_FUNDS;

        return buildTransactionResponse(transaction, description);
    }

    private TransactionResponse buildTransactionResponse(
            Transaction transaction, String description) {
        ExternalUserResponse sender = externalUserService.getByUserId(transaction.getSenderUserId());
        ExternalUserResponse recipient = externalUserService.getByUserId(transaction.getRecipientUserId());

        return TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .referenceId(transaction.getReferenceId())
                .description(description)
                .senderInfo(TransactionResponse.SenderInfo.builder()
                        .username(sender.getUsername())
                        .name(sender.getName())
                        .email(sender.getEmail())
                        .phoneNumber(sender.getPhone())
                        .senderNote(transaction.getNote())
                        .build())
                .recipientInfo(TransactionResponse.RecipientInfo.builder()
                        .username(recipient.getUsername())
                        .name(recipient.getName())
                        .email(recipient.getEmail())
                        .phoneNumber(recipient.getPhone())
                        .build())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .status(TransactionStatus.getDescription(transaction.getTransactionStatus()))
                .build();
    }
}
