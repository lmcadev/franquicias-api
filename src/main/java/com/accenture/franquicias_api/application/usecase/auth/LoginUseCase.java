package com.accenture.franquicias_api.application.usecase.auth;

import com.accenture.franquicias_api.application.dto.request.auth.AuthLoginRequest;
import com.accenture.franquicias_api.application.dto.response.auth.AuthTokenResponse;
import com.accenture.franquicias_api.application.mapper.user.UserMapper;
import com.accenture.franquicias_api.application.utils.ValidationUtils;
import com.accenture.franquicias_api.domain.repository.user.UserRepository;
import com.accenture.franquicias_api.infrastructure.security.JwtProvider;
import com.accenture.franquicias_api.presentation.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * UseCase para login de usuarios
 */
@Component
@RequiredArgsConstructor
public class LoginUseCase {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    
    public Mono<AuthTokenResponse> execute(AuthLoginRequest request) {
        // Validaciones
        ValidationUtils.validateEmail(request.getEmail());
        ValidationUtils.validatePassword(request.getPassword());
        
        // Buscar usuario por email
        return userRepository.findByEmail(request.getEmail())
            .switchIfEmpty(Mono.error(new UnauthorizedException("Email o contraseña incorrectos")))
            .flatMap(user -> {
                // Verificar contraseña
                boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
                if (!passwordMatches) {
                    return Mono.error(new UnauthorizedException("Email o contraseña incorrectos"));
                }
                
                // Verificar que usuario está activo
                if (!user.getActive()) {
                    return Mono.error(new UnauthorizedException("Usuario inactivo"));
                }
                
                // Generar JWT token con JwtProvider
                String token = jwtProvider.generateToken(user.getId(), user.getEmail());
                return Mono.just(userMapper.toResponse(user, "Bearer " + token));
            });
    }
}
