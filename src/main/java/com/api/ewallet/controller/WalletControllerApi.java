package com.api.ewallet.controller;

import com.api.ewallet.model.api.wallet.FriendListResponse;
import com.api.ewallet.model.api.wallet.WalletBalanceResponse;
import org.springframework.http.ResponseEntity;

public interface WalletControllerApi {

    ResponseEntity<FriendListResponse> getFriendList(String userId);

    ResponseEntity<WalletBalanceResponse> getWalletBalance(String userId);

}
