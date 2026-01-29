package com.api.ewallet.configuration.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("ewallet.external.user")
public class ExternalUserConfigProperties {

    @NotNull
    private String basePath;

    @NotNull
    private String getUsers;

}
