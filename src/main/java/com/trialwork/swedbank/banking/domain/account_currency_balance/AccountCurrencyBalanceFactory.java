package com.trialwork.swedbank.banking.domain.account_currency_balance;

import lombok.*;
import java.math.BigDecimal;
import com.trialwork.swedbank.banking.domain.currency.Currency;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountCurrencyBalanceFactory {

    public static AccountCurrencyBalance create(final String iban, final Currency currency) {
        final var balance = new AccountCurrencyBalance();
        balance.setIban(iban);
        balance.setCurrencyCode(currency.getCode());
        balance.setCurrency(currency);
        balance.setBalance(BigDecimal.ZERO);

        return balance;
    }
}