package com.trialwork.swedbank.banking.common;

import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;
import com.trialwork.swedbank.banking.common.exception.UnauthorizedException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthenticatedUserProvider {

    public static String getUserCode() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) throw new UnauthorizedException();

        return (String) authentication.getPrincipal();
    }
}