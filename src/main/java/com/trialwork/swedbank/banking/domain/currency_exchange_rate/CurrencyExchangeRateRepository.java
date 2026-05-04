package com.trialwork.swedbank.banking.domain.currency_exchange_rate;

import org.springframework.stereotype.Repository;
import com.trialwork.swedbank.banking.config.database.AppRepository;

@Repository
public interface CurrencyExchangeRateRepository extends AppRepository<CurrencyExchangeRate, String> {}