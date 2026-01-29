package com.api.ewallet.controller;

import com.api.ewallet.model.api.friend.FriendListResponse;
import org.springframework.http.ResponseEntity;

public interface WalletControllerApi {

    ResponseEntity<FriendListResponse> getFriendList(String userId);
}
