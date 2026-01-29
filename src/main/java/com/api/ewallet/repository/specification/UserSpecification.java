package com.api.ewallet.repository.specification;


import com.api.ewallet.model.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification extends BaseSpecification {

    private final static String USER_ID = "userId";
    private final static String USERNAME = "username";
    private final static String PHONE_NUMBER = "phoneNumber";

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

    /**
     * filter by username (username)
     *
     * @param username the username
     * @return Specification<User>
     */
    public static Specification<User> byUsername(String username) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isBlank(username)) {
                return criteriaBuilder.conjunction(); // Always true
            }
            return criteriaBuilder.equal(root.get(USERNAME), username);
        };
    }

    /**
     * filter by phoneNumber (phoneNumber)
     *
     * @param phoneNumber the phoneNumber
     * @return Specification<User>
     */
    public static Specification<User> byPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isBlank(phoneNumber)) {
                return criteriaBuilder.conjunction(); // Always true
            }
            return criteriaBuilder.equal(root.get(PHONE_NUMBER), phoneNumber);
        };
    }
}
