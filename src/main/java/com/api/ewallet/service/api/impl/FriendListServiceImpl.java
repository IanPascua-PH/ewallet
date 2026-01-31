package com.api.ewallet.service.api.impl;

import com.api.ewallet.enums.InvalidExceptionEnum;
import com.api.ewallet.exception.NotFoundException;
import com.api.ewallet.model.entity.User;
import com.api.ewallet.model.external.ExternalUserResponse;
import com.api.ewallet.model.wallet.FriendListResponse;
import com.api.ewallet.repository.UserRepository;
import com.api.ewallet.repository.specification.BaseSpecification;
import com.api.ewallet.repository.specification.UserSpecification;
import com.api.ewallet.service.api.FriendListService;
import com.api.ewallet.service.ws.ExternalUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendListServiceImpl implements FriendListService {

    private final ExternalUserService externalUserService;
    private final UserRepository userRepository;

    @Override
    public List<FriendListResponse.Friend> getFriendList(String userId){
        log.info("Retrieving friend list for userId: {}", userId);

        User currentUser = userRepository.findOne(UserSpecification.byUserId(userId)
                        .and(BaseSpecification.isActive()))
                .orElseThrow(() -> new NotFoundException(InvalidExceptionEnum.USER.getCode()));

        List<User> userList = userRepository.findAll();
        log.debug("Total users found: {}", userList.size() - 1);

        return userList.stream()
                .filter(user -> !user.getUserId().equals(currentUser.getUserId()))
                .map(this::mapToFriend)
                .toList();
    }

    private FriendListResponse.Friend mapToFriend(User user) {
        ExternalUserResponse externalUser = externalUserService.getByUserId(user.getUserId());;

        return FriendListResponse.Friend.builder()
                .userId(user.getUserId())
                .name(externalUser.getName())
                .userName(externalUser.getUsername())
                .email(externalUser.getEmail())
                .phoneNumber(externalUser.getPhone())
                .build();
    }
}
