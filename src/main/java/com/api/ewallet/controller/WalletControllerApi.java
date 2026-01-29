package com.api.ewallet.controller;

import com.api.ewallet.model.wallet.FriendListResponse;
import com.api.ewallet.model.wallet.SendMoneyRequest;
import com.api.ewallet.model.wallet.SendMoneyResponse;
import com.api.ewallet.model.wallet.WalletBalanceResponse;
import org.springframework.http.ResponseEntity;

public interface WalletControllerApi {

    ResponseEntity<FriendListResponse> getFriendList(String userId);

    ResponseEntity<WalletBalanceResponse> getWalletBalance(String userId);

    ResponseEntity<SendMoneyResponse> initiateSendMoney(String userId, String deviceName, SendMoneyRequest request);
}
