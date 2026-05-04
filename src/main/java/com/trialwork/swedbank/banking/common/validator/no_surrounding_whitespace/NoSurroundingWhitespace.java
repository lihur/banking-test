package com.trialwork.swedbank.banking.common.validator.no_surrounding_whitespace;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoSurroundingWhitespaceValidator.class)
public @interface NoSurroundingWhitespace {

    String message() default "must not have leading or trailing whitespace";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}