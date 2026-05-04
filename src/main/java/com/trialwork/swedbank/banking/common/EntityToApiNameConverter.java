package com.trialwork.swedbank.banking.common;

import lombok.*;
import com.trialwork.swedbank.banking.domain.account.Account;
import com.trialwork.swedbank.banking.domain.account_user.AccountUser;
import com.trialwork.swedbank.banking.domain.account_currency_balance.AccountCurrencyBalance;
import com.trialwork.swedbank.banking.domain.currency.Currency;
import com.trialwork.swedbank.banking.domain.currency_exchange_rate.CurrencyExchangeRate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityToApiNameConverter {

    public static String convert(final Class<?> clazz) {
        if (clazz == Account.class) return "account";
        if (clazz == AccountUser.class) return "account";
        if (clazz == AccountCurrencyBalance.class) return "balance";
        if (clazz == Currency.class) return "currency";
        if (clazz == CurrencyExchangeRate.class) return "rate";
        throw new IllegalArgumentException("Unsupported entity class: " + clazz.getName());
    }
}