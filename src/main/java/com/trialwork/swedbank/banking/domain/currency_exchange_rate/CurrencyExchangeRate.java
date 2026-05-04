package com.trialwork.swedbank.banking.domain.currency_exchange_rate;

import lombok.*;
import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
public class CurrencyExchangeRate {

    @Id
    @Column(length = 6)
    private String key;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal rate;
}