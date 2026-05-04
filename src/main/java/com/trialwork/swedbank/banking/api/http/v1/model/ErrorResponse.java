package com.trialwork.swedbank.banking.api.http.v1.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard API error response structure")
public record ErrorResponse(

    @Schema(description = "HTTP status code", example = "400")
    int status,

    @Schema(description = "General error message", example = "Validation failed")
    String message,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = "Detailed field-level validation errors", example = "{\"amount\": \"Must be greater than 0\"}")
    Map<String, String> errors
) {
    public ErrorResponse(final int status, final String message) {
        this(status, message, null);
    }
}