package com.api.ewallet.repository.specification;


import com.api.ewallet.model.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    private final static String USER_ID = "userId";

    private UserSpecification() {
    }


    /**
     * filter by userId (userId)
     *
     * @param userId the userId
     * @return Specification<User>
     */
    public static Specification<User> byUserId(String userId) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isBlank(userId)) {
                return criteriaBuilder.conjunction(); // Always true
            }
            return criteriaBuilder.equal(root.get(USER_ID), userId);
        };
    }
}
