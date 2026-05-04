package com.trialwork.swedbank.banking.api.http.v1.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import com.trialwork.swedbank.banking.common.validator.uppercase.Uppercase;
import com.trialwork.swedbank.banking.common.validator.no_surrounding_whitespace.NoSurroundingWhitespace;

@Schema(description = "Request model for currency exchange within the same account")
public record ExchangeRequest(

    @Schema(description = "Source currency code", example = "EUR", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Source currency is required")
    @Size(min = 3, max = 3, message = "Currency code must be 3 characters")
    @Uppercase(message = "Currency code must be uppercase")
    @NoSurroundingWhitespace(message = "Currency code must not contain surrounding whitespace")
    String fromCurrency,

    @Schema(description = "Target currency code", example = "USD", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Target currency is required")
    @Size(min = 3, max = 3, message = "Currency code must be 3 characters")
    @Uppercase(message = "Currency code must be uppercase")
    @NoSurroundingWhitespace(message = "Currency code must not contain surrounding whitespace")
    String toCurrency,

    @Schema(description = "Amount of source currency to exchange", example = "100.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    @Digits(integer = 13, fraction = 6, message = "Amount out of bounds (max 13 integer and 6 fraction digits)")
    BigDecimal amount
) {

    @AssertTrue(message = "Source and target currencies must be different")
    @JsonIgnore
    private boolean isCurrenciesDifferent() {
        if (fromCurrency == null || toCurrency == null) return true;

        return !fromCurrency.equalsIgnoreCase(toCurrency);
    }
}