package com.trialwork.swedbank.banking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/swagger-ui", "/swagger-ui/");
        registry.addViewController("/swagger-ui/").setViewName("forward:/swagger-ui/index.html");
    }
}