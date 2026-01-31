package com.api.ewallet.service.api;

import com.api.ewallet.model.wallet.FriendListResponse.Friend;

import java.util.List;

public interface FriendListService {

    List<Friend> getFriendList(String userId);

}
