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
 * UseCase para registrar nuevos usuarios
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
                
                // Guardar usuario
                return userRepository.save(user);
            })
            .map(savedUser -> {
                // Generar JWT token con JwtProvider
                String token = jwtProvider.generateToken(savedUser.getId(), savedUser.getEmail());
                return userMapper.toResponse(savedUser, "Bearer " + token);
            });
    }
}
