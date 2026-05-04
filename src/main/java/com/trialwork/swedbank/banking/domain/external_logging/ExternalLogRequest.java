package com.trialwork.swedbank.banking.domain.external_logging;

import lombok.Builder;
import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record ExternalLogRequest(
    String transactionType,
    String iban,
    String currencyCode,
    BigDecimal amount,
    String userCode,
    Instant timestamp
) {}