package com.api.ewallet.model.api.friend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendListResponse {

    private List<Friend> friendList;

    @Data
    @Builder
    public static class Friend {

        private String userId;

        private String name;

        private String userName;

        private String phoneNumber;

        private String email;
    }

}
