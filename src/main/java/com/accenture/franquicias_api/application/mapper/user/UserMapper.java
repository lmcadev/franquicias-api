package com.accenture.franquicias_api.application.mapper.user;

import com.accenture.franquicias_api.application.dto.request.auth.AuthLoginRequest;
import com.accenture.franquicias_api.application.dto.request.auth.AuthRegisterRequest;
import com.accenture.franquicias_api.application.dto.response.auth.AuthTokenResponse;
import com.accenture.franquicias_api.domain.entity.user.User;
import com.accenture.franquicias_api.domain.enums.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversión entre DTOs de autenticación y entidad de dominio Usuario.
 *
 * <p>
 * Utiliza MapStruct para generar implementaciones en tiempo de compilación.
 * Realiza conversiones entre:
 * <ul>
 *   <li>Solicitudes de registro ({@link AuthRegisterRequest}) → Entidad Usuario con rol USER</li>
 *   <li>Solicitudes de login ({@link AuthLoginRequest}) → Entidad Usuario</li>
 *   <li>Entidad Usuario → Respuesta con token JWT ({@link AuthTokenResponse})</li>
 * </ul>
 * </p>
 *
 * @see User
 * @see AuthRegisterRequest
 * @see AuthLoginRequest
 * @see AuthTokenResponse
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Convierte una solicitud de registro a entidad Usuario.
     * Establece automáticamente el rol como USER y activo como true.
     *
     * @param request la solicitud de registro
     * @return la entidad Usuario con valores del registro
     */
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "active", constant = "true")
    User toDomain(AuthRegisterRequest request);

    /**
     * Convierte una solicitud de login a entidad Usuario.
     *
     * @param request la solicitud de login
     * @return la entidad Usuario con valores del login
     */
    User toDomainFromLogin(AuthLoginRequest request);

    /**
     * Convierte una entidad Usuario y token JWT a respuesta de autenticación.
     * Establece automáticamente duración de expiración y tipo de token.
     *
     * @param user la entidad Usuario autenticada
     * @param token el token JWT generado
     * @return la respuesta con token y metadatos
     */
    @Mapping(target = "expiresIn", expression = "java(86400000L)")
    @Mapping(target = "tokenType", constant = "Bearer")
    AuthTokenResponse toResponse(User user, String token);
}
