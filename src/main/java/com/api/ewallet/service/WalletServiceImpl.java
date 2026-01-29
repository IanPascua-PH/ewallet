package com.api.ewallet.service;

import com.api.ewallet.exception.NotFoundException;
import com.api.ewallet.model.api.wallet.FriendListResponse.Friend;
import com.api.ewallet.model.api.wallet.WalletBalanceResponse;
import com.api.ewallet.model.api.wallet.WalletBalanceResponse.*;
import com.api.ewallet.model.entity.User;
import com.api.ewallet.model.entity.Wallet;
import com.api.ewallet.model.external.ExternalUserResponse;
import com.api.ewallet.repository.UserRepository;
import com.api.ewallet.repository.WalletRepository;
import com.api.ewallet.repository.specification.UserSpecification;
import com.api.ewallet.repository.specification.WalletSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletServiceImpl implements WalletService {

    private final ExternalUserService externalUserService;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    private final static String USER = "User";
    private final static String WALLET = "Wallet";

    @Override
    public List<Friend> getFriendList(String userId){
        log.info("Retrieving friend list for userId: {}", userId);

        User currentUser = userRepository.findOne(UserSpecification.byUserId(userId)).orElseThrow(() -> new NotFoundException(USER));

        List<User> userList = userRepository.findAll();
        log.debug("Total users found: {}", userList.size() - 1);

        return userList.stream()
                .filter(user -> !user.getUserId().equals(currentUser.getUserId()))
                .map(this::mapToFriend)
                .toList();
    }

    private Friend mapToFriend(User user) {
        ExternalUserResponse externalUser = externalUserService.getByUserId(user.getUserId());;

        return Friend.builder()
                .userId(user.getUserId())
                .name(externalUser.getName())
                .userName(externalUser.getUsername())
                .email(externalUser.getEmail())
                .phoneNumber(externalUser.getPhone())
                .build();
    }

    @Override
    public WalletBalanceResponse getWalletBalance(String userId) {
        log.info("Retrieving wallet balance for userId: {}", userId);

        User currentUser = userRepository.findOne(UserSpecification.byUserId(userId))
                .orElseThrow(() -> new NotFoundException(USER));
        Wallet wallet = walletRepository.findOne(WalletSpecification.byUserId(currentUser.getUserId()))
                .orElseThrow(() -> new NotFoundException(WALLET));


        log.debug("Wallet balance for user {}: {} {}", userId, wallet.getBalance(), wallet.getCurrency());

        return WalletBalanceResponse.builder()
                .walletId(wallet.getWalletId())
                .availableBalance(AvailableBalance.builder()
                        .amount(wallet.getBalance())
                        .currency(wallet.getCurrency())
                        .build())
                .limits(Limit.builder()
                        .dailyLimit(wallet.getDailyLimit())
                        .build())
                .walletStatus(wallet.getWalletStatus())
                .lastUpdated(wallet.getUpdatedAt())
                .build();
    }


}
