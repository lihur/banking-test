package com.trialwork.swedbank.banking.common.validator.iban;

import jakarta.validation.*;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidIbanValidator.class)
public @interface ValidIban {

    String message() default "Invalid IBAN format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}