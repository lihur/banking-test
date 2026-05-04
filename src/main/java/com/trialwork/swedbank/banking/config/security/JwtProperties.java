package com.trialwork.swedbank.banking.config.security;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    @NotBlank(message = "JWT public key must be configured")
    private String publicKey;
}