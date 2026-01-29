package com.api.ewallet.model.wallet;

import com.api.ewallet.validator.UsernameOrPhoneRequired;
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

    private String username;

    private String phoneNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than 0")
    private BigDecimal amount;

    private String note;
}
