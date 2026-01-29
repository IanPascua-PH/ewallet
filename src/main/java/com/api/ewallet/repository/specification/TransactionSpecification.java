package com.api.ewallet.repository.specification;

import com.api.ewallet.model.entity.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TransactionSpecification {

    private static final String SENDER_WALLET_ID = "senderWalletId";
    private static final String TRANSACTION_STATUS = "transactionStatus";
    private static final String CREATED_AT = "createdAt";
    private static final String SENDER_USER_ID = "senderUserId";
    private static final String RECIPIENT_USER_ID = "recipientUserId";

    private TransactionSpecification() {
    }

    /* Specification to filter by sender wallet ID */
    public static Specification<Transaction> bySenderWalletId(String senderWalletId) {
        return (root, query, criteriaBuilder) -> {
            if (senderWalletId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(SENDER_WALLET_ID), senderWalletId);
        };
    }

    public static Specification<Transaction> byTransactionStatus(String transactionStatus) {
        return (root, query, criteriaBuilder) -> {
            if (transactionStatus == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(TRANSACTION_STATUS), transactionStatus);
        };
    }

    /**
     * Specification to filter by createdAt between startDate and endDate
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Specification<Transaction> byCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null || endDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.between(root.get(CREATED_AT), startDate, endDate);
        };
    }

    /**
     * Specification to filter by sender user ID
     *
     * @param senderUserId
     * @return
     */
    public static Specification<Transaction> bySenderUserId(String senderUserId) {
        return (root, query, criteriaBuilder) -> {
            if (senderUserId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(SENDER_USER_ID), senderUserId);
        };
    }

    /**
     * Specification to filter by recipient user ID
     *
     * @param recipientUserId
     * @return
     */
    public static Specification<Transaction> byRecipientUserId(String recipientUserId) {
        return (root, query, criteriaBuilder) -> {
            if (recipientUserId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(RECIPIENT_USER_ID), recipientUserId);
        };
    }

}
