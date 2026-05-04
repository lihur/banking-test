package com.trialwork.swedbank.banking.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnauthorizedException extends ApiException {

    private static final String MESSAGE = "Unauthorized";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException() {
        super(STATUS, MESSAGE);
    }

    public UnauthorizedException(final Throwable cause) {
        super(STATUS, MESSAGE, cause);
    }
}