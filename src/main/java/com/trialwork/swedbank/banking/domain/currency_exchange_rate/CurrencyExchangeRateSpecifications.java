package com.trialwork.swedbank.banking.domain.currency_exchange_rate;

import lombok.*;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyExchangeRateSpecifications {

    public static Specification<CurrencyExchangeRate> byKey(final String key) {
        return (root, query, cb) -> cb.equal(root.get(CurrencyExchangeRate_.key), key);
    }
}