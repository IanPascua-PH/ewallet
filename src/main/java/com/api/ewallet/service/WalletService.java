package com.api.ewallet.service;

import java.util.List;
import com.api.ewallet.model.wallet.FriendListResponse.Friend;
import com.api.ewallet.model.wallet.SendMoneyRequest;
import com.api.ewallet.model.wallet.SendMoneyResponse;
import com.api.ewallet.model.wallet.WalletBalanceResponse;

public interface WalletService {

    List<Friend> getFriendList(String userId);

    WalletBalanceResponse getWalletBalance(String userId);

    SendMoneyResponse initiateSendMoney(String userId, String deviceName, SendMoneyRequest request);

}
