package com.trialwork.swedbank.banking.common.validator.no_surrounding_whitespace;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoSurroundingWhitespaceValidator implements ConstraintValidator<NoSurroundingWhitespace, String> {

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return value == null || value.equals(value.trim());
    }
}