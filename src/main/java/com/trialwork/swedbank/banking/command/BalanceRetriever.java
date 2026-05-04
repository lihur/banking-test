package com.trialwork.swedbank.banking.command;

import java.util.List;

import com.trialwork.swedbank.banking.domain.account_currency_balance.AccountCurrencyBalance;
import com.trialwork.swedbank.banking.domain.account_currency_balance.AccountCurrencyBalanceRetriever;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.trialwork.swedbank.banking.common.AuthenticatedUserProvider;
import com.trialwork.swedbank.banking.domain.account_user.AccountUserRetriever;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceRetriever {

    private final AccountUserRetriever accountUserRetriever;
    private final AccountCurrencyBalanceRetriever accountCurrencyBalanceRetriever;

    public List<AccountCurrencyBalance> retrieve(final String iban) {
        log.debug("Retrieving account balance for iban {}", iban);

        final var userCode = AuthenticatedUserProvider.getUserCode();
        accountUserRetriever.verifyExists(iban, userCode);

        return accountCurrencyBalanceRetriever.findAllByIban(iban);
    }
}