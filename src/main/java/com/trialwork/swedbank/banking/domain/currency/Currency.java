package com.trialwork.swedbank.banking.domain.currency;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
public class Currency {

    @Id
    @Column(length = 3)
    private String code;
}