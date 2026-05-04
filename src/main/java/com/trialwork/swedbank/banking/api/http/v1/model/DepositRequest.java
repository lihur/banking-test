package com.trialwork.swedbank.banking.api.http.v1.model;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.trialwork.swedbank.banking.common.validator.uppercase.Uppercase;
import com.trialwork.swedbank.banking.common.validator.no_surrounding_whitespace.NoSurroundingWhitespace;

@Schema(description = "Request model for depositing money into an account")
public record DepositRequest(

    @Schema(description = "ISO 4217 currency code", example = "EUR", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Currency code is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    @Uppercase(message = "Currency code must be uppercase")
    @NoSurroundingWhitespace(message = "Currency code must not contain surrounding whitespace")
    String currency,

    @Schema(description = "Amount to deposit", example = "150.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    @Digits(integer = 13, fraction = 6, message = "Amount is out of bounds (expected <13 digits>.<6 digits>)")
    BigDecimal amount
) {}