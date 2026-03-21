package com.accenture.franquicias_api.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Validador de configuración JWT.
 * 
 * Verifica que JWT_SECRET esté correctamente configurado antes de que se inicie
 * cualquier operación de autenticación o inicialización de admin.
 * 
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtConfigValidator {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @EventListener(ApplicationReadyEvent.class)
    public void validateJwtConfiguration() {
        log.info("Iniciando validación de configuración JWT...");

        // Validar que JWT_SECRET está configurado
        if (jwtSecret == null || jwtSecret.isBlank()) {
            String errorMsg = "ERROR CRÍTICO: JWT_SECRET no está configurado. " +
                    "La aplicación no puede iniciar sin esta variable de entorno. " +
                    "Asegúrate de que:\n" +
                    "   1. La variable JWT_SECRET está en el archivo .env\n" +
                    "   2. Docker compose está leyendo el archivo .env: env_file: .env\n" +
                    "   3. La variable se está pasando al contenedor correctamente\n" +
                    "   4. NO hay fallback en application.yml (deber ser ${JWT_SECRET} sin :default)";
            
            log.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        if (jwtSecret.length() < 32) {
            String warnMsg = "ADVERTENCIA: JWT_SECRET es muy corto (" + jwtSecret.length() + 
                    " caracteres). Debería tener al menos 32 caracteres para seguridad adecuada.";
            log.warn(warnMsg);
        }

        // Log información de validación exitosa
        log.info("Configuración JWT validada exitosamente");
        log.info("   JWT_SECRET: {}...(longitud: {} caracteres)", 
                jwtSecret.substring(0, Math.min(20, jwtSecret.length())), 
                jwtSecret.length());
        log.info("   AdminInitializer derivará contraseña de JWT_SECRET: {}", 
                jwtSecret.substring(0, Math.min(12, jwtSecret.length())));
    }
}
