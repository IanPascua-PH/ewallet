package com.api.ewallet.service;

import java.util.List;
import com.api.ewallet.model.api.wallet.FriendListResponse.Friend;
import com.api.ewallet.model.api.wallet.WalletBalanceResponse;

public interface WalletService {

    List<Friend> getFriendList(String userId);

    WalletBalanceResponse getWalletBalance(String userId);

}
