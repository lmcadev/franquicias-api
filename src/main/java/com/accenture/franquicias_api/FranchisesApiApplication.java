package com.accenture.franquicias_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.accenture.franquicias_api")
public class FranchisesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FranchisesApiApplication.class, args);
    }
}
