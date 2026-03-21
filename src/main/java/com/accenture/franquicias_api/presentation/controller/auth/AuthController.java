package com.accenture.franquicias_api.presentation.controller.auth;

import com.accenture.franquicias_api.application.dto.request.auth.AuthLoginRequest;
import com.accenture.franquicias_api.application.dto.request.auth.AuthRegisterRequest;
import com.accenture.franquicias_api.application.dto.response.auth.AuthTokenResponse;
import com.accenture.franquicias_api.application.usecase.auth.LoginUseCase;
import com.accenture.franquicias_api.application.usecase.auth.RegisterUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticación", description = "Endpoints para registro e inicio de sesión de usuarios")
public class AuthController {
    
    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    
    /**
     * Registrar nuevo usuario
     * POST /api/auth/register
     */
    @PostMapping("/register")
    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Crea una nueva cuenta de usuario y retorna token JWT"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario registrado exitosamente",
            content = @Content(schema = @Schema(implementation = AuthTokenResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o email duplicado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
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
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario y retorna token JWT para requests posteriores"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login exitoso",
            content = @Content(schema = @Schema(implementation = AuthTokenResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Credenciales inválidas o usuario no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    public Mono<ResponseEntity<AuthTokenResponse>> login(
            @Valid @RequestBody AuthLoginRequest request) {
        return loginUseCase.execute(request)
            .map(response -> ResponseEntity.ok(response));
    }
}
