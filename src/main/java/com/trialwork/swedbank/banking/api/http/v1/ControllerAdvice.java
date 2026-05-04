package com.trialwork.swedbank.banking.api.http.v1;

import java.util.*;
import java.util.stream.Collectors;
import jakarta.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import tools.jackson.databind.exc.MismatchedInputException;
import com.trialwork.swedbank.banking.common.exception.*;
import com.trialwork.swedbank.banking.api.http.v1.model.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(final UnauthorizedException e) {
        return buildResponse(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(UnprocessableContentException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(final UnprocessableContentException e) {
        return buildResponse(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(final NotFoundException e) {
        final var params = e.getParams().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue())));

        return buildResponse(e.getStatus(), e.getMessage(), params);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(final ApiException e) {
        return buildResponse(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(final ConstraintViolationException e) {
        final var errors = e.getConstraintViolations().stream()
            .collect(Collectors.toMap(
                violation -> violation.getPropertyPath().toString(),
                ConstraintViolation::getMessage,
                (existing, replacement) -> existing + "; " + replacement
            ));

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        final var errors = e.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                this::mapFieldError,
                (existing, replacement) -> existing + "; " + replacement
            ));

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleReadableException(final HttpMessageNotReadableException e) {
        final var field = getHttpMessageNotReadableExceptionField(e);
        final var details = getHttpMessageNotReadableExceptionValue(e);

        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", Map.of(field, details));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(final Exception e) {
        log.error(e.getMessage(), e);

        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        return buildResponse(status, message, Collections.emptyMap());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, Map<String, String> details) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message, details));
    }

    private String mapFieldError(final FieldError f) {
        if ("typeMismatch".equals(f.getCode())) {
            final var tme = f.unwrap(TypeMismatchException.class);
            final String expected = tme.getRequiredType() != null ? tme.getRequiredType().getSimpleName() : "unknown";
            final String actual = tme.getValue() != null ? tme.getValue().toString() : "null";

            return "Expected " + expected + " but got " + actual;
        }

        return Objects.requireNonNullElse(f.getDefaultMessage(), "Invalid value");
    }

    private String getHttpMessageNotReadableExceptionField(final HttpMessageNotReadableException ex) {
        final var cause = ex.getMostSpecificCause();
        if (cause instanceof MismatchedInputException mismatch && mismatch.getPath() != null) {
            return mismatch.getPath().stream()
                .map(ref -> ref.getPropertyName() != null ? ref.getPropertyName() : String.valueOf(ref.getIndex()))
                .collect(Collectors.joining("."));
        }

        return "body";
    }

    private String getHttpMessageNotReadableExceptionValue(final HttpMessageNotReadableException ex) {
        final var cause = ex.getMostSpecificCause();
        if (cause instanceof MismatchedInputException mismatch) {
            final String expected = mismatch.getTargetType() != null ? mismatch.getTargetType().getSimpleName() : "unknown";

            return "Invalid format. Expected type: " + expected;
        }

        return "Check JSON syntax";
    }
}