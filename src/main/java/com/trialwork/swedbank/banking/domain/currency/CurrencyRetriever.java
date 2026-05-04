package com.trialwork.swedbank.banking.domain.currency;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.trialwork.swedbank.banking.common.EntityToApiNameConverter;
import com.trialwork.swedbank.banking.common.exception.NotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyRetriever {

    private final CurrencyRepository repository;

    public Currency get(final String code) {
        log.debug("Retrieving currency {}", code);

        return repository.findOne(CurrencySpecifications.byCode(code))
            .orElseThrow(() -> new NotFoundException(
                EntityToApiNameConverter.convert(Currency.class),
                Map.of("code", code)
            ));
    }
}