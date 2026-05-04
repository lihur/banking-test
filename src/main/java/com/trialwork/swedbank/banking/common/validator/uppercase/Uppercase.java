package com.trialwork.swedbank.banking.common.validator.uppercase;

import jakarta.validation.*;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UppercaseValidator.class)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Uppercase {

    String message() default "must be uppercase";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}