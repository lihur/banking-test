package com.trialwork.swedbank.banking.domain.account_user;

import lombok.*;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountUserSpecifications {

    public static Specification<AccountUser> byIban(final String iban) {
        return (root, query, cb) -> cb.equal(root.get(AccountUser_.iban), iban);
    }

    public static Specification<AccountUser> byUserCode(final String userCode) {
        return (root, query, cb) -> cb.equal(root.get(AccountUser_.userCode), userCode);
    }
}