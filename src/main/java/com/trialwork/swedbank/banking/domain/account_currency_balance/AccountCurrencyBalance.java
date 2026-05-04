package com.trialwork.swedbank.banking.domain.account_currency_balance;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import com.trialwork.swedbank.banking.domain.account.Account;
import com.trialwork.swedbank.banking.domain.currency.Currency;

@Getter
@Setter
@Entity
@IdClass(AccountCurrencyBalance.PrimaryKey.class)
public class AccountCurrencyBalance {

    @Id
    @Column(length = 34)
    private String iban;

    @Id
    @Column(name = "currency_code", length = 3)
    private String currencyCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "iban", insertable = false, updatable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_code", insertable = false, updatable = false)
    private Currency currency;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal balance = BigDecimal.ZERO;

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class PrimaryKey implements Serializable {

        private String iban;
        private String currencyCode;
    }
}