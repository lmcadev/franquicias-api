package com.accenture.franquicias_api.application.utils;

/**
 * Constantes de la aplicación
 */
public class AppConstants {

    // Reglas de validación
    public static final int NAME_MIN_LENGTH = 1;
    public static final int NAME_MAX_LENGTH = 100;
    public static final int MIN_STOCK = 0;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 255;

    // Mensajes comunes
    public static final String NAME_REQUIRED = "El nombre es requerido";
    public static final String NAME_TOO_LONG = "El nombre no puede exceder " + NAME_MAX_LENGTH + " caracteres";
    public static final String NAME_TOO_SHORT = "El nombre debe tener al menos " + NAME_MIN_LENGTH + " carácter";
    public static final String INVALID_STOCK = "El stock no puede ser negativo";
    public static final String EMAIL_REQUIRED = "El email es requerido";
    public static final String INVALID_EMAIL = "El email debe ser válido";
    public static final String PASSWORD_REQUIRED = "La contraseña es requerida";
    public static final String PASSWORD_TOO_SHORT = "La contraseña debe tener mínimo " + MIN_PASSWORD_LENGTH + " caracteres";

    // Paginación por defecto
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int MAX_PAGE_SIZE = 100;

    // Rol por defecto
    public static final String DEFAULT_USER_ROLE = "USER";

    private AppConstants() {
        // Clase de constantes
    }
}
