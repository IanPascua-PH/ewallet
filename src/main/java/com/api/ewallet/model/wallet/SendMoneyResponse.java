package com.api.ewallet.model.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendMoneyResponse {

    private String transactionId;

    private String referenceId;

    private BigDecimal amount;

    private String currency;

    private String status;

    private String note;

    private String deviceName;

    private SenderInfo senderInfo;

    private RecipientInfo recipientInfo;

    @Data
    @Builder
    public static class SenderInfo {

        private String username;

        private String name;

        private String email;

        private String phoneNumber;

    }

    @Data
    @Builder
    public static class RecipientInfo {

        private String username;

        private String name;

        private String email;

        private String phoneNumber;

    }
}
