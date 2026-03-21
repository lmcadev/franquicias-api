package com.accenture.franquicias_api.application.utils;

import com.accenture.franquicias_api.presentation.exception.InvalidInputException;
import java.util.regex.Pattern;

/**
 * Utilidad de validaciones para campos de entrada comunes en la API.
 *
 * <p>
 * Proporciona métodos estáticos para validar:
 * <ul>
 *   <li>Nombres: no vacíos, máximo 100 caracteres</li>
 *   <li>Emails: formato válido según patrón regex</li>
 *   <li>Contraseñas: longitud mínima de 8 caracteres</li>
 *   <li>Stock: no negativo (>= 0)</li>
 *   <li>Paginación: página >= 0, tamaño entre 1 y 100</li>
 * </ul>
 * </p>
 *
 * <p>
 * Lanza {@link InvalidInputException} con detalles del campo si la validación falla.
 * </p>
 *
 * <p>
 * Ejemplo:
 * <pre>
 * ValidationUtils.validateEmail(request.getEmail());
 * ValidationUtils.validatePassword(request.getPassword());
 * </pre>
 * </p>
 */
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );

    public static void validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException(fieldName, "No puede estar vacío");
        }
        if (name.length() > AppConstants.NAME_MAX_LENGTH) {
            throw new InvalidInputException(fieldName, "No puede exceder " + AppConstants.NAME_MAX_LENGTH + " caracteres");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidInputException("email", "No puede estar vacío");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidInputException("email", "Formato inválido");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("password", "No puede estar vacía");
        }
        if (password.length() < AppConstants.MIN_PASSWORD_LENGTH) {
            throw new InvalidInputException("password", "Debe tener mínimo " + AppConstants.MIN_PASSWORD_LENGTH + " caracteres");
        }
    }

    public static void validateStock(Integer stock) {
        if (stock == null || stock < AppConstants.MIN_STOCK) {
            throw new InvalidInputException("stock", "No puede ser negativo");
        }
    }

    public static void validatePageNumber(Integer page) {
        if (page == null || page < 0) {
            throw new InvalidInputException("page", "Debe ser mayor o igual a 0");
        }
    }

    public static void validatePageSize(Integer size) {
        if (size == null || size <= 0) {
            throw new InvalidInputException("size", "Debe ser mayor a 0");
        }
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new InvalidInputException("size", "No puede exceder " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    private ValidationUtils() {
        // Clase de utilidades
    }
}
