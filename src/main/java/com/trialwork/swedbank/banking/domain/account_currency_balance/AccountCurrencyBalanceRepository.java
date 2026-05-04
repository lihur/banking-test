package com.trialwork.swedbank.banking.domain.account_currency_balance;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCurrencyBalanceRepository
    extends JpaRepository<AccountCurrencyBalance, AccountCurrencyBalance.PrimaryKey>, JpaSpecificationExecutor<AccountCurrencyBalance> {}
