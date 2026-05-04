package com.trialwork.swedbank.banking.domain.account_user;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountUserRepository extends JpaRepository<AccountUser, AccountUser.PrimaryKey>, JpaSpecificationExecutor<AccountUser> {}
