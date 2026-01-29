package com.api.ewallet.configuration.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
@ConfigurationProperties("ewallet.wallet")
public class WalletConfigProperties {

    @NotNull
    private BigDecimal dailyLimit;

}
