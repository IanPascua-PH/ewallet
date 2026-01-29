package com.api.ewallet.controller;

import com.api.ewallet.model.wallet.*;
import org.springframework.http.ResponseEntity;

public interface WalletControllerApi {

    ResponseEntity<FriendListResponse> getFriendList(String userId);

    ResponseEntity<WalletBalanceResponse> getWalletBalance(String userId);

    ResponseEntity<SendMoneyResponse> initiateSendMoney(String userId, SendMoneyRequest request);

    ResponseEntity<TransactionResponse> getTransactionDetails(String userId, String txnId);

}
