package br.edu.unievangelica.smartcity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Projeto didatico: Smart City - UniEVANGELICA
 *
 * Aplicacao Spring Boot + Thymeleaf + H2 que ilustra, na pratica,
 * conceitos de Smart City / IoT com dashboards em tempo real
 * (Server-Sent Events) para 7 modulos de sensoriamento urbano.
 */
@SpringBootApplication
@EnableScheduling
public class SmartCityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCityApplication.class, args);
    }

}
