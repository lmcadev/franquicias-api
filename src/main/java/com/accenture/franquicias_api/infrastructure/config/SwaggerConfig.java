package com.accenture.franquicias_api.infrastructure.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI 3.1 para la API de Franquicias.
 *
 * <p>
 * Define la información y seguridad de la documentación automática de la API:
 * <ul>
 *   <li>Título, descripción y versión de la API</li>
 *   <li>Información de contacto y licencia</li>
 *   <li>Esquema de seguridad JWT en headers Authorization</li>
 *   <li>Generación automática de DocumentObjectModel (OpenAPI bean)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Accesible en:
 * <ul>
 *   <li>Swagger UI: http://localhost:8080/swagger-ui.html</li>
 *   <li>JSON OpenAPI: http://localhost:8080/v3/api-docs</li>
 * </ul>
 * </p>
 *
 * @see org.springdoc.openapi.starter.webflux.api.OpenApiWebfluxResource
 */
@Configuration
@SecurityScheme(
    name = "Authorization",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT token obtenido del endpoint /api/auth/login",
    in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(apiInfo())
            .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }

    private Info apiInfo() {
        return new Info()
            .title("Franquicias API")
            .description("API REST para gestión de franquicias, sucursales y productos con autenticación JWT")
            .version("1.0.0")
            .contact(contact())
            .license(license());
    }

    private Contact contact() {
        return new Contact()
            .name("Prueba técnica para Accenture")
            .email("lmcadev@gmail.com")
            .url("https://www.lmcadev.com");
    }

    private License license() {
        return new License()
            .name("Apache 2.0")
            .url("https://www.apache.org/licenses/LICENSE-2.0.html");
    }
}
