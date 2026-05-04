package com.trialwork.swedbank.banking.config.openapi;

import lombok.*;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "app.apidoc")
public class OpenApiProperties {

    @NotBlank
    private String title;

    @NotBlank
    private String version;

    private String description;

    private String contactName;

    @Email
    private String contactEmail;
}
