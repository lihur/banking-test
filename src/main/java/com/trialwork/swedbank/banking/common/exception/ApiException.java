package com.trialwork.swedbank.banking.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
    }

    public ApiException(final HttpStatus status, final String message, final Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}