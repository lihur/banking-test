package com.trialwork.swedbank.banking.config.database;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"com.trialwork.swedbank.banking.domain.**"})
@EnableJpaRepositories(
    basePackages = {"com.trialwork.swedbank.banking.domain"},
    repositoryBaseClass = AppRepositoryImpl.class
)
public class DatabaseConfig {
}
