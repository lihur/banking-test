package com.trialwork.swedbank.banking.common.exception;

import org.springframework.http.HttpStatus;

public class UnprocessableContentException extends ApiException {

    private static final HttpStatus STATUS = HttpStatus.UNPROCESSABLE_CONTENT;

    public UnprocessableContentException(final String message) {
        super(STATUS, message);
    }
}