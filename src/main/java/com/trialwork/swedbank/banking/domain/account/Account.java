package com.trialwork.swedbank.banking.domain.account;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
public class Account {

    @Id
    @Column(length = 34)
    private String iban;
}