package com.api.ewallet.model.wallet;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletBalanceResponse {

    private String walletId;

    private AvailableBalance availableBalance;

    private Limit limits;

    private String walletStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated;

    @Data
    @Builder
    public static class AvailableBalance {

        private BigDecimal amount;

        private String currency;

    }

    @Data
    @Builder
    public static class Limit {

        private BigDecimal dailyLimit;

    }
}
