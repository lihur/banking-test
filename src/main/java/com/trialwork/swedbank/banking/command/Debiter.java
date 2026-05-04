package com.trialwork.swedbank.banking.command;

import java.math.BigDecimal;
import java.time.Instant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.trialwork.swedbank.banking.common.*;
import com.trialwork.swedbank.banking.common.exception.*;
import com.trialwork.swedbank.banking.domain.account_currency_balance.*;
import com.trialwork.swedbank.banking.domain.account_user.AccountUserRetriever;
import com.trialwork.swedbank.banking.domain.external_logging.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class Debiter {

    private final AccountUserRetriever accountUserRetriever;
    private final AccountCurrencyBalanceRetriever accountCurrencyBalanceRetriever;
    private final ExternalLoggingService externalLoggingService;
    private final AccountCurrencyBalanceRepository repository;

    @Transactional
    public void debit(final String iban, final String currencyCode, final BigDecimal amount) {
        log.debug("Debiting {} {} from account {}", amount, currencyCode, iban);

        final var userCode = AuthenticatedUserProvider.getUserCode();
        accountUserRetriever.verifyExists(iban, userCode);
        final var balance = accountCurrencyBalanceRetriever.find(iban, currencyCode)
            .orElseThrow(() -> new UnprocessableContentException("Insufficient funds for debit operation"));

        if (balance.getBalance().compareTo(amount) < 0) {
            throw new UnprocessableContentException("Insufficient funds for debit operation");
        }

        balance.setBalance(balance.getBalance().subtract(amount));

        repository.save(balance);

        externalLoggingService.log(
            ExternalLogRequest.builder()
                .transactionType("DEBIT")
                .iban(iban)
                .currencyCode(currencyCode)
                .amount(amount)
                .userCode(userCode)
                .timestamp(Instant.now())
                .build()
        );
    }
}