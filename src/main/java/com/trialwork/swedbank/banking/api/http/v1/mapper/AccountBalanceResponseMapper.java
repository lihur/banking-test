package com.trialwork.swedbank.banking.api.http.v1.mapper;

import lombok.*;
import java.util.Collection;
import com.trialwork.swedbank.banking.api.http.v1.model.AccountBalanceResponse;
import com.trialwork.swedbank.banking.domain.account_currency_balance.AccountCurrencyBalance;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountBalanceResponseMapper {

    public static AccountBalanceResponse map(final Collection<AccountCurrencyBalance> balances) {
        final var response = new AccountBalanceResponse();
        if (balances == null) return response;

        balances.forEach(b -> response.put(b.getCurrencyCode(), b.getBalance()));

        return response;
    }
}