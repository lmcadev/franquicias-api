package com.accenture.franquicias_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Aplicación principal de Spring Boot para Franquicias API.
 *
 * <p>
 * Punto de entrada de la aplicación que inicializa:
 * <ul>
 *   <li>Contexto de Spring Boot 3 con WebFlux (reactive)</li>
 *   <li>Configuración de seguridad con JWT</li>
 *   <li>Persistencia con R2DBC y MySQL</li>
 *   <li>Documentación OpenAPI 3.1 con Swagger</li>
 *   <li>Validaciones y mapeo de datos</li>
 * </ul>
 * </p>
 *
 * <p>
 * Ejecutar con: {@code mvn spring-boot:run}
 * </p>
 *
 * <p>
 * La aplicación escucha en puerto configurado (por defecto 8080)
 * y expone endpoints en /api/...
 * </p>
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.accenture.franquicias_api")
public class FranchisesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FranchisesApiApplication.class, args);
    }
}
