package com.trialwork.swedbank.banking.common.validator.iban;

import jakarta.validation.*;

public class ValidIbanValidator implements ConstraintValidator<ValidIban, String> {

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (value == null || value.length() < 15 || value.length() > 34) return false;

        final var countryCodeValid = value.substring(0, 2).chars().allMatch(c -> c >= 'A' && c <= 'Z');
        if (!countryCodeValid) return false;

        final var checksumValid = value.substring(2, 4).chars().allMatch(c -> c >= '0' && c <= '9');
        if (!checksumValid) return false;

        final var bbanValid = value.substring(4).chars().allMatch(c -> (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z'));
        if (!bbanValid) return false;

        final var rearranged = value.substring(4) + value.substring(0, 4);

        final var remainder = rearranged.chars()
            .map(Character::getNumericValue)
            .reduce(0, (r, digit) -> digit > 9 ? ((r * 100) + digit) % 97 : ((r * 10) + digit) % 97);

        return remainder == 1;
    }
}