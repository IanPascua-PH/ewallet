package com.api.ewallet.service;

import java.util.List;
import com.api.ewallet.model.api.friend.FriendListResponse.Friend;

public interface WalletService {

    List<Friend> getFriendList(String userId);

}
