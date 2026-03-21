package com.franquicias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.franquicias")
public class FranchisesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FranchisesApiApplication.class, args);
    }
}
