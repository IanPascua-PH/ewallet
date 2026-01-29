package com.api.ewallet.service;

import com.api.ewallet.model.wallet.FriendListResponse.Friend;
import com.api.ewallet.model.wallet.SendMoneyRequest;
import com.api.ewallet.model.wallet.SendMoneyResponse;
import com.api.ewallet.model.wallet.TransactionResponse;
import com.api.ewallet.model.wallet.WalletBalanceResponse;

import java.util.List;

public interface WalletService {

    List<Friend> getFriendList(String userId);

    WalletBalanceResponse getWalletBalance(String userId);

    SendMoneyResponse initiateSendMoney(String userId, SendMoneyRequest request);

    TransactionResponse getTransactionDetails(String userId, String transactionId);

}
