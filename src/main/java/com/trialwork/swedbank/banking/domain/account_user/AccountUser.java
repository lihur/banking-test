package com.trialwork.swedbank.banking.domain.account_user;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import com.trialwork.swedbank.banking.domain.account.Account;

@Entity
@Getter
@Setter
@IdClass(AccountUser.PrimaryKey.class)
public class AccountUser {

    @Id
    @Column(length = 34)
    private String iban;

    @Id
    @Column(length = 10)
    private String userCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "iban", insertable = false, updatable = false)
    private Account account;

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class PrimaryKey implements Serializable {

        private String iban;
        private String userCode;
    }
}