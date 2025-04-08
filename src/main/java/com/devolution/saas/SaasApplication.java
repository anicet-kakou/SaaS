package com.devolution.saas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Copyright (c) 2025 DEVOLUTION. Tous droits réservés.
 * SaaS - Plateforme API multi-tenant
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableAspectJAutoProxy
public class SaasApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaasApplication.class, args);
    }

}
