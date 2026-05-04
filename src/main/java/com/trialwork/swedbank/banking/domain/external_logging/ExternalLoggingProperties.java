package com.trialwork.swedbank.banking.domain.external_logging;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "app.external-logging")
public class ExternalLoggingProperties {

    @NotBlank
    private String url;
}