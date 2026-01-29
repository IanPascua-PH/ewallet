package com.api.ewallet.service;

import com.api.ewallet.exception.NotFoundException;
import com.api.ewallet.model.api.friend.FriendListResponse.Friend;
import com.api.ewallet.model.entity.User;
import com.api.ewallet.model.external.ExternalUserResponse;
import com.api.ewallet.repository.UserRepository;
import com.api.ewallet.repository.specification.UserSpecification;
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

    @Override
    public List<Friend> getFriendList(String userId){
        log.info("Retrieving friend list for userId: {}", userId);

        User currentUser = userRepository.findOne(UserSpecification.byUserId(userId)).orElseThrow(() -> new NotFoundException("User not found"));

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
}
