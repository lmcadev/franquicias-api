package com.accenture.franquicias_api.domain.enums;

/**
 * Enumeración de roles de usuario para control de acceso basado en roles (RBAC).
 *
 * <p>
 * Define tres niveles de autorización en la aplicación:
 * <ul>
 *   <li><b>ADMIN</b>: Administrador con acceso total a todas las operaciones</li>
 *   <li><b>USER</b>: Usuario regular con acceso a operaciones básicas</li>
 *   <li><b>READ_ONLY</b>: Solo lectura, sin permisos de escritura</li>
 * </ul>
 * </p>
 *
 * <p>
 * Utilizado por {@link com.accenture.franquicias_api.infrastructure.security.SecurityFilter}
 * y {@link com.accenture.franquicias_api.infrastructure.config.SecurityConfig} para
 * validar autorización en endpoints.
 * </p>
 */
public enum UserRole {
    ADMIN("Administrador - acceso total"),
    USER("Usuario regular - acceso básico"),
    READ_ONLY("Solo lectura");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
