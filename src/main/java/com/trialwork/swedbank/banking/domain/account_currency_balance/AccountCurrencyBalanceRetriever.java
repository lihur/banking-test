package com.trialwork.swedbank.banking.domain.account_currency_balance;

import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountCurrencyBalanceRetriever {

    private final AccountCurrencyBalanceRepository repository;

    public Optional<AccountCurrencyBalance> find(final String iban, final String currencyCode) {
        log.debug("Retrieving account currency balance for iban {} and currencyCode {}", iban, currencyCode);

        final var spec = AccountCurrencyBalanceSpecifications.byIban(iban)
            .and(AccountCurrencyBalanceSpecifications.byCurrencyCode(currencyCode));

        return repository.findOne(spec);
    }

    public List<AccountCurrencyBalance> findAllByIban(final String iban) {
        log.debug("Retrieving all account currency balances for iban {}", iban);

        final var spec = AccountCurrencyBalanceSpecifications.byIban(iban)
            .and(AccountCurrencyBalanceSpecifications.withNonZeroBalance());

        return repository.findAll(spec, Sort.by(AccountCurrencyBalance_.CURRENCY_CODE));
    }
}