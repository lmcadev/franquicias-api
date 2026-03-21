package com.accenture.franquicias_api.presentation.controller.auth;

import com.accenture.franquicias_api.application.dto.request.auth.AuthLoginRequest;
import com.accenture.franquicias_api.application.dto.request.auth.AuthRegisterRequest;
import com.accenture.franquicias_api.application.dto.response.auth.AuthTokenResponse;
import com.accenture.franquicias_api.application.usecase.auth.LoginUseCase;
import com.accenture.franquicias_api.application.usecase.auth.RegisterUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

/**
 * Controller para autenticación de usuarios
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    
    /**
     * Registrar nuevo usuario
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public Mono<ResponseEntity<AuthTokenResponse>> register(
            @Valid @RequestBody AuthRegisterRequest request) {
        return registerUseCase.execute(request)
            .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }
    
    /**
     * Login de usuario
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<AuthTokenResponse>> login(
            @Valid @RequestBody AuthLoginRequest request) {
        return loginUseCase.execute(request)
            .map(response -> ResponseEntity.ok(response));
    }
}
