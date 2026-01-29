package com.api.ewallet.repository.specification;

import com.api.ewallet.model.entity.Wallet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class WalletSpecification {
    private final static String USER_ID = "userId";

    private WalletSpecification() {
    }

   /** Specification to filter Wallets by userId.
    * @param userId the user ID to filter by
    * @return Specification for filtering Wallets by userId
    *
    */
    public static Specification<Wallet> byUserId(String userId) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isBlank(userId)) {
                return criteriaBuilder.conjunction(); // Always true
            }
            return criteriaBuilder.equal(root.get(USER_ID), userId);
        };
    }
}
