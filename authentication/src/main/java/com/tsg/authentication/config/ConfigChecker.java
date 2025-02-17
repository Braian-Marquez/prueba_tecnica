package com.tsg.authentication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ConfigChecker {

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @PostConstruct
    public void verifyConsulConfig() {
        System.out.println("🔹 Configuración obtenida de Consul:");
        System.out.println("🔹 URL de la base de datos: " + databaseUrl);
        System.out.println("🔹 Usuario: " + databaseUsername);
        
        if (databaseUrl == null || databaseUsername == null) {
            throw new IllegalStateException("⚠ ERROR: La configuración de la base de datos no se cargó desde Consul.");
        }
    }
}

