package com.api.ewallet.model.wallet;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String transactionId;

    private String referenceId;

    private BigDecimal amount;

    private String currency;

    private String status;

    private String description;

    private SenderInfo senderInfo;

    private RecipientInfo recipientInfo;

    @Data
    @Builder
    public static class SenderInfo {

        private String username;

        private String name;

        private String email;

        private String phoneNumber;

        private String senderNote;

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
