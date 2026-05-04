package com.trialwork.swedbank.banking.api.http.v1.model;

import java.util.HashMap;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonIgnoreProperties({"empty"})
@Schema(
    description = "Map of currency codes to balances",
    example = "{\"EUR\": 100.000000, \"USD\": 50.500000}",
    additionalPropertiesSchema = BigDecimal.class
)
public class AccountBalanceResponse extends HashMap<String, BigDecimal> {}