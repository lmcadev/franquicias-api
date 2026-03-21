package com.accenture.franquicias_api.application.usecase.auth;

import com.accenture.franquicias_api.application.dto.request.auth.AuthRegisterRequest;
import com.accenture.franquicias_api.application.dto.response.auth.AuthTokenResponse;
import com.accenture.franquicias_api.application.mapper.user.UserMapper;
import com.accenture.franquicias_api.application.utils.ValidationUtils;
import com.accenture.franquicias_api.domain.entity.user.User;
import com.accenture.franquicias_api.domain.enums.UserRole;
import com.accenture.franquicias_api.domain.repository.user.UserRepository;
import com.accenture.franquicias_api.infrastructure.security.JwtProvider;
import com.accenture.franquicias_api.presentation.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Caso de uso para registro de nuevos usuarios.
 *
 * <p>
 * Realiza las siguientes operaciones:
 * <ul>
 *   <li>Valida el formato del email y contraseña</li>
 *   <li>Verifica que el email no esté ya registrado</li>
 *   <li>Crea un nuevo usuario con rol USER</li>
 *   <li>Encripta la contraseña usando BCrypt</li>
 *   <li>Marca el usuario como activo</li>
 *   <li>Persiste el usuario en la base de datos</li>
 * </ul>
 * </p>
 *
 * <p>
 * Lanza excepciones:
 * <ul>
 *   <li>{@link ConflictException} si el email ya existe en el sistema</li>
 * </ul>
 * </p>
 *
 * @see AuthRegisterRequest
 * @see AuthTokenResponse
 */
@Component
@RequiredArgsConstructor
public class RegisterUseCase {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    
    public Mono<AuthTokenResponse> execute(AuthRegisterRequest request) {
        // Validaciones
        ValidationUtils.validateEmail(request.getEmail());
        ValidationUtils.validatePassword(request.getPassword());
        ValidationUtils.validateName(request.getName(), "name");
        
        // Verificar que el email no exista
        return userRepository.findByEmail(request.getEmail())
            .hasElement()
            .flatMap(emailExists -> {
                if (emailExists) {
                    return Mono.error(new ConflictException("Usuario", "email", request.getEmail()));
                }
                
                // Mapear DTO a entidad de dominio
                User user = userMapper.toDomain(request);
                user.setRole(UserRole.USER);
                user.setActive(true);
                
                // Hash de contraseña
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                
                // Guardar usuario y luego recuperarlo con su ID asignado
                return userRepository.save(user)
                    .flatMap(savedUser -> userRepository.findByEmail(savedUser.getEmail()));
            })
            .map(savedUser -> {
                // Generar JWT token con JwtProvider
                String token = jwtProvider.generateToken(savedUser.getId(), savedUser.getEmail());
                return userMapper.toResponse(savedUser, token);
            });
    }
}
