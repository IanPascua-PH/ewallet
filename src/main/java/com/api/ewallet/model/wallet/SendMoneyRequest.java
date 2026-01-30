package com.api.ewallet.model.wallet;

import com.api.ewallet.validator.UsernameOrPhoneRequired;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UsernameOrPhoneRequired
public class SendMoneyRequest {

    @Schema(example = "username1", description = "Recipient's username", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(example = "+639123456789", description = "Recipient's phone number", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phoneNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than 0")
    private BigDecimal amount;

    @Schema(example = "For your health", description = "Message note", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String note;
}
