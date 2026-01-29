package com.api.ewallet.controller;

import com.api.ewallet.model.api.friend.FriendListResponse;
import com.api.ewallet.model.api.friend.FriendListResponse.Friend;
import com.api.ewallet.service.WalletService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/wallet")
public class WalletController implements WalletControllerApi {

    private final WalletService walletService;

    @Override
    @GetMapping("/getFriendList")
    public ResponseEntity<FriendListResponse> getFriendList(@Parameter(description = "User ID passed in header", required = true)
                                                             @RequestHeader("X-User-Id") String userId) {
        log.info("GET /api/friend - userId: {}", userId);
        List<Friend> friendList = walletService.getFriendList(userId);
        FriendListResponse response = FriendListResponse.builder()
                .friendList(friendList)
                .build();
        return ResponseEntity.ok(response);
    }
}
