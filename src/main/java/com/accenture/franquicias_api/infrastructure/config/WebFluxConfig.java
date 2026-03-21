package com.accenture.franquicias_api.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Configuración de Spring WebFlux para operaciones reactivas.
 *
 * <p>
 * Establece:
 * <ul>
 *   <li>CORS (Cross-Origin Resource Sharing) para todas las rutas</li>
 *   <li>Método HTTP permitidos: GET, POST, PUT, DELETE, PATCH, OPTIONS</li>
 *   <li>Límite de tamaño en memoria: 16 MB para payloads grandes</li>
 *   <li>Max-Age de caché CORS: 3600 segundos (1 hora)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Necesaria para soporte de operaciones no-bloqueantes con Project Reactor.
 * </p>
 */
@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .maxAge(3600);
    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024);
    }
}
