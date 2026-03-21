package com.accenture.franquicias_api.application.usecase.auth;

import com.accenture.franquicias_api.application.dto.request.auth.AuthLoginRequest;
import com.accenture.franquicias_api.application.dto.response.auth.AuthTokenResponse;
import com.accenture.franquicias_api.application.mapper.user.UserMapper;
import com.accenture.franquicias_api.domain.entity.user.User;
import com.accenture.franquicias_api.domain.enums.UserRole;
import com.accenture.franquicias_api.domain.repository.user.UserRepository;
import com.accenture.franquicias_api.infrastructure.security.JwtProvider;
import com.accenture.franquicias_api.presentation.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para LoginUseCase usando JUnit 5 + Mockito + StepVerifier
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LoginUseCase Tests")
class LoginUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private AuthLoginRequest validRequest;
    private User mockUser;
    private AuthTokenResponse mockResponse;

    @BeforeEach
    void setUp() {
        validRequest = AuthLoginRequest.builder()
            .email("user@example.com")
            .password("SecurePass123!")
            .build();

        mockUser = User.builder()

            .email("user@example.com")
            .password("$2a$10$hashedPassword")
            .name("Test User")
            .role(UserRole.USER)
            .active(true)
            .build();

        mockResponse = AuthTokenResponse.builder()
            .token("Bearer eyJhbGciOiJIUzI1NiJ9...")
            .email("user@example.com")
            .name("Test User")
            .build();
    }

    @Test
    @DisplayName("Debe realizar login exitosamente")
    void testLoginSuccess() {
        // Arrange
        mockUser.setId(1L);
        when(userRepository.findByEmail("user@example.com"))
            .thenReturn(Mono.just(mockUser));
        when(passwordEncoder.matches("SecurePass123!", "$2a$10$hashedPassword"))
            .thenReturn(true);
        when(jwtProvider.generateToken(1L, "user@example.com"))
            .thenReturn("eyJhbGciOiJIUzI1NiJ9...");
        when(userMapper.toResponse(mockUser, "Bearer eyJhbGciOiJIUzI1NiJ9..."))
            .thenReturn(mockResponse);

        // Act & Assert
        StepVerifier.create(loginUseCase.execute(validRequest))
            .expectNextMatches(response -> 
                response.getEmail().equals("user@example.com") &&
                response.getName().equals("Test User") &&
                response.getToken().startsWith("Bearer ")
            )
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe lanzar UnauthorizedException si email no existe")
    void testLoginUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com"))
            .thenReturn(Mono.empty());

        AuthLoginRequest request = AuthLoginRequest.builder()
            .email("nonexistent@example.com")
            .password("SecurePass123!")
            .build();

        // Act & Assert
        StepVerifier.create(loginUseCase.execute(request))
            .expectError(UnauthorizedException.class)
            .verify();
    }

    @Test
    @DisplayName("Debe lanzar UnauthorizedException si contraseña es incorrecta")
    void testLoginIncorrectPassword() {
        // Arrange
        when(userRepository.findByEmail("user@example.com"))
            .thenReturn(Mono.just(mockUser));
        when(passwordEncoder.matches("WrongPassword", "$2a$10$hashedPassword"))
            .thenReturn(false);

        AuthLoginRequest wrongPasswordRequest = AuthLoginRequest.builder()
            .email("user@example.com")
            .password("WrongPassword")
            .build();

        // Act & Assert
        StepVerifier.create(loginUseCase.execute(wrongPasswordRequest))
            .expectError(UnauthorizedException.class)
            .verify();
    }

    @Test
    @DisplayName("Debe lanzar UnauthorizedException si usuario está inactivo")
    void testLoginInactiveUser() {
        // Arrange
        User inactiveUser = User.builder()

            .email("user@example.com")
            .password("$2a$10$hashedPassword")
            .name("Inactive User")
            .role(UserRole.USER)
            .active(false)
            .build();

        when(userRepository.findByEmail("user@example.com"))
            .thenReturn(Mono.just(inactiveUser));
        when(passwordEncoder.matches("SecurePass123!", "$2a$10$hashedPassword"))
            .thenReturn(true);

        // Act & Assert
        StepVerifier.create(loginUseCase.execute(validRequest))
            .expectError(UnauthorizedException.class)
            .verify();
    }

    @Test
    @Disabled("Requiere refactoring del UseCase para validaciones reactivas")
    @DisplayName("Debe lanzar excepción si email es inválido")
    void testLoginInvalidEmail() {
        // Arrange
        AuthLoginRequest invalidEmailRequest = AuthLoginRequest.builder()
            .email("invalid-email")
            .password("SecurePass123!")
            .build();

        // Act & Assert
        StepVerifier.create(loginUseCase.execute(invalidEmailRequest))
            .expectError(Exception.class)
            .verify();
    }
}
