package com.api.ewallet.repository.specification;

import com.api.ewallet.model.entity.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TransactionSpecification {

    private static final String SENDER_WALLET_ID = "senderWalletId";
    private static final String TRANSACTION_STATUS = "transactionStatus";
    private static final String CREATED_AT = "createdAt";

    private TransactionSpecification() {
    }

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

    public static Specification<Transaction> byCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null || endDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.between(root.get(CREATED_AT), startDate, endDate);
        };
    }
}
