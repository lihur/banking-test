package com.trialwork.swedbank.banking.domain.account_user;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.trialwork.swedbank.banking.common.EntityToApiNameConverter;
import com.trialwork.swedbank.banking.common.exception.NotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountUserRetriever {

    private final AccountUserRepository repository;

    public void verifyExists(final String iban, final String userCode) {
        log.debug("Verifying account user exists for iban {} and userCode {}", iban, userCode);

        final var spec = AccountUserSpecifications.byIban(iban)
            .and(AccountUserSpecifications.byUserCode(userCode));

        if (!repository.exists(spec)) {
            throw new NotFoundException(
                EntityToApiNameConverter.convert(AccountUser.class),
                Map.of("iban", iban, "userCode", userCode)
            );
        }
    }
}