package com.trialwork.swedbank.banking.domain.currency_exchange_rate;

import java.math.BigDecimal;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.trialwork.swedbank.banking.common.EntityToApiNameConverter;
import com.trialwork.swedbank.banking.common.exception.NotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyExchangeRateRetriever {

    private final CurrencyExchangeRateRepository repository;

    public BigDecimal get(final String fromCurrencyCode, final String toCurrencyCode) {
        log.debug("Retrieving exchange rate from {} to {}", fromCurrencyCode, toCurrencyCode);
        final var key = (fromCurrencyCode + toCurrencyCode).toUpperCase();

        return repository.findOne(CurrencyExchangeRateSpecifications.byKey(key), CurrencyExchangeRate_.rate)
            .orElseThrow(() -> new NotFoundException(
                EntityToApiNameConverter.convert(CurrencyExchangeRate.class),
                Map.of("fromCurrencyCode", fromCurrencyCode, "toCurrencyCode", toCurrencyCode)
            ));
    }
}