package com.api.ewallet.integration.job;

import com.api.ewallet.EWalletApplicationTests;
import com.api.ewallet.enums.TransactionStatus;
import com.api.ewallet.job.TransactionScheduledJob;
import com.api.ewallet.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionScheduledJobIT extends EWalletApplicationTests {

    @Autowired
    private TransactionScheduledJob transactionScheduledJob;

    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    void testUpdateStuckPendingTransactions() {
        when(transactionRepository.updateStuckPendingTransactions(
                eq(TransactionStatus.PENDING.getCode()),
                eq(TransactionStatus.FAILED.getCode()),
                any())).thenReturn(5);

        transactionScheduledJob.updateStuckTransactions();

        verify(transactionRepository, times(2)).updateStuckPendingTransactions(
                eq(TransactionStatus.PENDING.getCode()),
                eq(TransactionStatus.FAILED.getCode()),
                any());
    }

    @Test
    void testUpdateStuckPendingTransactions_NoUpdates() {
        when(transactionRepository.updateStuckPendingTransactions(
                eq(TransactionStatus.PENDING.getCode()),
                eq(TransactionStatus.FAILED.getCode()),
                any())).thenReturn(0);

        transactionScheduledJob.updateStuckTransactions();

        verify(transactionRepository, times(1)).updateStuckPendingTransactions(
                eq(TransactionStatus.PENDING.getCode()),
                eq(TransactionStatus.FAILED.getCode()),
                any());
    }
}
