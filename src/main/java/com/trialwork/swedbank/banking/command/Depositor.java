package com.trialwork.swedbank.banking.command;

import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.trialwork.swedbank.banking.common.AuthenticatedUserProvider;
import com.trialwork.swedbank.banking.domain.currency.*;
import com.trialwork.swedbank.banking.domain.account_user.AccountUserRetriever;
import com.trialwork.swedbank.banking.domain.account_currency_balance.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class Depositor {

    private final AccountUserRetriever accountUserRetriever;
    private final CurrencyRetriever currencyRetriever;
    private final AccountCurrencyBalanceRetriever accountCurrencyBalanceRetriever;
    private final AccountCurrencyBalanceRepository repository;

    @Transactional
    public void deposit(final String iban, final String currencyCode, final BigDecimal amount) {
        log.debug("Depositing {} {} to account {}", amount, currencyCode, iban);
        final var userCode = AuthenticatedUserProvider.getUserCode();
        accountUserRetriever.verifyExists(iban, userCode);

        final var currency = currencyRetriever.get(currencyCode);
        final var balance = accountCurrencyBalanceRetriever.find(iban, currencyCode)
            .orElseGet(() -> AccountCurrencyBalanceFactory.create(iban, currency));
        balance.setBalance(balance.getBalance().add(amount));

        repository.save(balance);
    }
}