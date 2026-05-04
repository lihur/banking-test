package com.trialwork.swedbank.banking.domain.external_logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalLoggingService {

    private final ExternalLoggingProperties properties;
    private final RestClient restClient;

    public void log(final ExternalLogRequest request) {
        log.debug("Calling external logging service with request: {}", request);

        restClient.post()
            .uri(properties.getUrl())
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .toBodilessEntity();
    }
}