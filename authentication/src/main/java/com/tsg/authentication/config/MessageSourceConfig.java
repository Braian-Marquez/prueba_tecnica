package com.tsg.authentication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class MessageSourceConfig {

    @Value("${api.path.messages}")
    private String path;

    @Bean
    ResourceBundleMessageSource messageSource() {        
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames(path);
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultLocale(Locale.US);
        return source;
    }
}
