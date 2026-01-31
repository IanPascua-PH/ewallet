package com.api.ewallet.service.api.impl;

import com.api.ewallet.enums.ActiveStatus;
import com.api.ewallet.enums.InvalidExceptionEnum;
import com.api.ewallet.exception.NotFoundException;
import com.api.ewallet.model.entity.User;
import com.api.ewallet.model.entity.Wallet;
import com.api.ewallet.model.wallet.WalletBalanceResponse;
import com.api.ewallet.repository.UserRepository;
import com.api.ewallet.repository.WalletRepository;
import com.api.ewallet.repository.specification.BaseSpecification;
import com.api.ewallet.repository.specification.UserSpecification;
import com.api.ewallet.repository.specification.WalletSpecification;
import com.api.ewallet.service.api.InquireBalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquireBalanceServiceImpl implements InquireBalanceService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Override
    public WalletBalanceResponse inquireBalance(String userId) {
        log.info("Retrieving wallet balance for userId: {}", userId);

        User currentUser = userRepository.findOne(UserSpecification.byUserId(userId)
                        .and(BaseSpecification.isActive()))
                .orElseThrow(() -> new NotFoundException(InvalidExceptionEnum.USER.getCode()));
        Wallet wallet = walletRepository.findOne(WalletSpecification.byUserId(currentUser.getUserId())
                        .and(BaseSpecification.isActive()))
                .orElseThrow(() -> new NotFoundException(InvalidExceptionEnum.WALLET.getCode()));

        log.debug("Wallet balance for user {}: {} {}", userId, wallet.getBalance(), wallet.getCurrency());

        return WalletBalanceResponse.builder()
                .walletId(wallet.getWalletId())
                .availableBalance(WalletBalanceResponse.AvailableBalance.builder()
                        .amount(wallet.getBalance())
                        .currency(wallet.getCurrency())
                        .build())
                .limits(WalletBalanceResponse.Limit.builder()
                        .dailyLimit(wallet.getDailyLimit())
                        .build())
                .walletStatus(ActiveStatus.getDescription(wallet.getStatus()))
                .lastUpdated(wallet.getUpdatedAt())
                .build();
    }
}
