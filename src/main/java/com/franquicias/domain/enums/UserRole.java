package com.franquicias.domain.enums;

/**
 * Enumeración de roles de usuario para RBAC
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
