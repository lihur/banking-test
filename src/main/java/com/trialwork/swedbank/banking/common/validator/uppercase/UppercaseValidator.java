package com.trialwork.swedbank.banking.common.validator.uppercase;

import jakarta.validation.*;

public class UppercaseValidator implements ConstraintValidator<Uppercase, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        return value.equals(value.toUpperCase());
    }
}