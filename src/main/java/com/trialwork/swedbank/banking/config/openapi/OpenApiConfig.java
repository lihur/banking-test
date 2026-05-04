package com.trialwork.swedbank.banking.config.openapi;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;

@Slf4j
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI openApi(final OpenApiProperties properties) {
        log.info("OpenAPI configuration loaded");

        return new OpenAPI()
            .info(createInfo(properties))
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
            .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, createSecurityScheme()));
    }

    private static Info createInfo(final OpenApiProperties properties) {
        return new Info()
            .title(properties.getTitle())
            .version(properties.getVersion())
            .description(properties.getDescription())
            .contact(createContact(properties));
    }

    private static Contact createContact(final OpenApiProperties properties) {
        final var output = new Contact();

        output.setName(properties.getContactName());
        output.setEmail(properties.getContactEmail());

        return output;
    }

    private static SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
            .name(SECURITY_SCHEME_NAME)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .description("Enter your HS256 JWT token to authenticate requests.");
    }
}