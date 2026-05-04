package com.trialwork.swedbank.banking.command;

import java.math.*;
import java.util.List;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.trialwork.swedbank.banking.common.AuthenticatedUserProvider;
import com.trialwork.swedbank.banking.common.exception.UnprocessableContentException;
import com.trialwork.swedbank.banking.domain.account_currency_balance.*;
import com.trialwork.swedbank.banking.domain.account_user.AccountUserRetriever;
import com.trialwork.swedbank.banking.domain.currency.CurrencyRetriever;
import com.trialwork.swedbank.banking.domain.currency_exchange_rate.CurrencyExchangeRateRetriever;

@Slf4j
@Service
@RequiredArgsConstructor
public class Exchanger {

    private final AccountUserRetriever accountUserRetriever;
    private final AccountCurrencyBalanceRetriever accountCurrencyBalanceRetriever;
    private final CurrencyRetriever currencyRetriever;
    private final CurrencyExchangeRateRetriever currencyExchangeRateRetriever;
    private final AccountCurrencyBalanceRepository repository;

    @Transactional
    public void exchange(final String iban, final String fromCurrencyCode, final String toCurrencyCode, final BigDecimal amount) {
        log.debug("Exchanging {} {} to {} for account {}", amount, fromCurrencyCode, toCurrencyCode, iban);

        final var userCode = AuthenticatedUserProvider.getUserCode();
        accountUserRetriever.verifyExists(iban, userCode);

        final var fromBalance = accountCurrencyBalanceRetriever.find(iban, fromCurrencyCode)
            .orElseThrow(() -> new UnprocessableContentException("Insufficient funds for debit operation"));
        if (fromBalance.getBalance().compareTo(amount) < 0) {
            throw new UnprocessableContentException("Insufficient funds for debit operation");
        }

        final var rate = currencyExchangeRateRetriever.get(fromCurrencyCode, toCurrencyCode);
        final var convertedAmount = amount.multiply(rate).setScale(6, RoundingMode.HALF_UP);

        final var toCurrency = currencyRetriever.get(toCurrencyCode);
        final var toBalance = accountCurrencyBalanceRetriever.find(iban, toCurrencyCode)
            .orElseGet(() -> AccountCurrencyBalanceFactory.create(iban, toCurrency));

        fromBalance.setBalance(fromBalance.getBalance().subtract(amount));
        toBalance.setBalance(toBalance.getBalance().add(convertedAmount));

        repository.saveAll(List.of(fromBalance, toBalance));
    }
}