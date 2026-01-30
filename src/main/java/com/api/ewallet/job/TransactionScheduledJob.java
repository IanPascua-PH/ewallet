package com.api.ewallet.job;

import com.api.ewallet.configuration.properties.WalletConfigProperties;
import com.api.ewallet.enums.TransactionStatus;
import com.api.ewallet.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionScheduledJob {

    private final TransactionRepository transactionRepository;

    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void updateStuckTransactions() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(3); // Transactions older than 3 minutes

        int updatedCount = transactionRepository.updateStuckPendingTransactions(
                TransactionStatus.PENDING.getCode(),
                TransactionStatus.FAILED.getCode(),
                cutoffTime);

        log.info("Updated {} stuck pending transactions", updatedCount);
    }
}
