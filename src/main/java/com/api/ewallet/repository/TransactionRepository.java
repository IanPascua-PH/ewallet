package com.api.ewallet.repository;

import com.api.ewallet.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    @Modifying
    @Query("UPDATE Transaction SET transactionStatus = :failedStatus WHERE transactionStatus = :pendingStatus AND createdAt < :cutoffTime")
    int updateStuckPendingTransactions(String pendingStatus, String failedStatus, LocalDateTime cutoffTime);
}
