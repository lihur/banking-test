package com.trialwork.swedbank.banking.domain.account_currency_balance;

import lombok.*;
import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountCurrencyBalanceSpecifications {

    public static Specification<AccountCurrencyBalance> byIban(final String iban) {
        return (root, query, cb) -> cb.equal(root.get(AccountCurrencyBalance_.iban), iban);
    }

    public static Specification<AccountCurrencyBalance> byCurrencyCode(final String currencyCode) {
        return (root, query, cb) -> cb.equal(root.get(AccountCurrencyBalance_.currencyCode), currencyCode);
    }

    public static Specification<AccountCurrencyBalance> withNonZeroBalance() {
        return (root, query, cb) -> cb.greaterThan(root.get(AccountCurrencyBalance_.balance), BigDecimal.ZERO);
    }
}