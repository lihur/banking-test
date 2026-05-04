package com.trialwork.swedbank.banking.common.exception;

import lombok.Getter;
import java.util.Map;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends ApiException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private final String object;
    private final Map<String, Object> params;

    public NotFoundException(final String object, final Map<String, Object> params) {
        super(STATUS, "%s not found for %s".formatted(object, params));
        this.object = object;
        this.params = params;
    }

    public NotFoundException(final String object, final Map<String, Object> params, final Throwable cause) {
        super(STATUS, "%s not found for %s".formatted(object, params), cause);
        this.object = object;
        this.params = params;
    }
}