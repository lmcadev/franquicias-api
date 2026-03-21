package com.accenture.franquicias_api.presentation.controller.auth;

import com.accenture.franquicias_api.application.dto.request.auth.AuthLoginRequest;
import com.accenture.franquicias_api.application.dto.request.auth.AuthRegisterRequest;
import com.accenture.franquicias_api.application.dto.response.auth.AuthTokenResponse;
import com.accenture.franquicias_api.application.usecase.auth.LoginUseCase;
import com.accenture.franquicias_api.application.usecase.auth.RegisterUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para AuthController usando JUnit 5 + Mockito + StepVerifier
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Mock
    private RegisterUseCase registerUseCase;

    @Mock
    private LoginUseCase loginUseCase;

    @InjectMocks
    private AuthController authController;

    private AuthRegisterRequest registerRequest;
    private AuthLoginRequest loginRequest;
    private AuthTokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        registerRequest = AuthRegisterRequest.builder()
            .email("newuser@example.com")
            .password("SecurePass123!")
            .name("New User")
            .build();

        loginRequest = AuthLoginRequest.builder()
            .email("user@example.com")
            .password("SecurePass123!")
            .build();

        tokenResponse = AuthTokenResponse.builder()
            .token("Bearer eyJhbGciOiJIUzI1NiJ9...")
            .email("user@example.com")
            .name("Test User")
            .build();
    }

    @Test
    @DisplayName("Debe registrar usuario y retornar 201 Created")
    void testRegisterSuccess() {
        // Arrange
        when(registerUseCase.execute(registerRequest))
            .thenReturn(Mono.just(tokenResponse));

        // Act
        Mono<ResponseEntity<AuthTokenResponse>> response = authController.register(registerRequest);

        // Assert
        StepVerifier.create(response)
            .expectNextMatches(entity -> 
                entity.getStatusCode() == HttpStatus.CREATED &&
                entity.getBody().getEmail().equals("user@example.com") &&
                entity.getBody().getToken().startsWith("Bearer ")
            )
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe realizar login y retornar 200 OK")
    void testLoginSuccess() {
        // Arrange
        when(loginUseCase.execute(loginRequest))
            .thenReturn(Mono.just(tokenResponse));

        // Act
        Mono<ResponseEntity<AuthTokenResponse>> response = authController.login(loginRequest);

        // Assert
        StepVerifier.create(response)
            .expectNextMatches(entity -> 
                entity.getStatusCode() == HttpStatus.OK &&
                entity.getBody().getEmail().equals("user@example.com") &&
                entity.getBody().getToken().startsWith("Bearer ")
            )
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar error si registro falla")
    void testRegisterError() {
        // Arrange
        when(registerUseCase.execute(any()))
            .thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act & Assert
        Mono<ResponseEntity<AuthTokenResponse>> response = authController.register(registerRequest);
        StepVerifier.create(response)
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    @DisplayName("Debe retornar error si login falla")
    void testLoginError() {
        // Arrange
        when(loginUseCase.execute(any()))
            .thenReturn(Mono.error(new RuntimeException("Invalid credentials")));

        // Act & Assert
        Mono<ResponseEntity<AuthTokenResponse>> response = authController.login(loginRequest);
        StepVerifier.create(response)
            .expectError(RuntimeException.class)
            .verify();
    }
}
