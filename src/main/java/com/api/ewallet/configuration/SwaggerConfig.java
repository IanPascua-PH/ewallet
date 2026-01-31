package com.api.ewallet.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI walletControllerApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Wallet App")
                        .version("1.0.0")
                        .description("Application for managing e-wallet operations like send money, get transaction details, check friend list and balance inquiry"))
                .components(new Components()
                        .addSecuritySchemes("userIdAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-User-Id")))
                .addSecurityItem(new SecurityRequirement().addList("userIdAuth"));
    }
}
