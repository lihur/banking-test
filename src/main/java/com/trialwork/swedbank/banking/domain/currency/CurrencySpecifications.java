package com.trialwork.swedbank.banking.domain.currency;

import lombok.*;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CurrencySpecifications {

    public static Specification<Currency> byCode(final String code) {
        return (root, query, cb) -> cb.equal(root.get(Currency_.code), code);
    }
}
