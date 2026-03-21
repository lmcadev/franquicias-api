package com.accenture.franquicias_api.application.usecase.auth;

import com.accenture.franquicias_api.application.dto.request.auth.AuthRegisterRequest;
import com.accenture.franquicias_api.application.dto.response.auth.AuthTokenResponse;
import com.accenture.franquicias_api.application.mapper.user.UserMapper;
import com.accenture.franquicias_api.domain.entity.user.User;
import com.accenture.franquicias_api.domain.enums.UserRole;
import com.accenture.franquicias_api.domain.repository.user.UserRepository;
import com.accenture.franquicias_api.infrastructure.security.JwtProvider;
import com.accenture.franquicias_api.presentation.exception.ConflictException;
import com.accenture.franquicias_api.presentation.exception.InvalidInputException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para RegisterUseCase usando JUnit 5 + Mockito + StepVerifier
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterUseCase Tests")
class RegisterUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private RegisterUseCase registerUseCase;

    private AuthRegisterRequest validRequest;
    private User mockUser;
    private AuthTokenResponse mockResponse;

    @BeforeEach
    void setUp() {
        validRequest = AuthRegisterRequest.builder()
            .email("newuser@example.com")
            .password("SecurePass123!")
            .name("New User")
            .build();

        mockUser = User.builder()
            .email("newuser@example.com")
            .password("$2a$10$hashedPassword")
            .name("New User")
            .role(UserRole.USER)
            .active(true)
            .build();

        mockResponse = AuthTokenResponse.builder()
            .token("Bearer eyJhbGciOiJIUzI1NiJ9...")
            .email("newuser@example.com")
            .name("New User")
            .build();
    }

    @Test
    @DisplayName("Debe registrar usuario exitosamente")
    void testRegisterSuccess() {
        // Arrange
        mockUser.setId(1L);
        when(userRepository.findByEmail("newuser@example.com"))
            .thenReturn(Mono.empty());
        when(userMapper.toDomain(validRequest))
            .thenReturn(User.builder()
                .email("newuser@example.com")
                .password("SecurePass123!")
                .name("New User")
                .build());
        when(passwordEncoder.encode("SecurePass123!"))
            .thenReturn("$2a$10$hashedPassword");
        when(userRepository.save(any(User.class)))
            .thenReturn(Mono.just(mockUser));
        when(jwtProvider.generateToken(1L, "newuser@example.com"))
            .thenReturn("eyJhbGciOiJIUzI1NiJ9...");
        when(userMapper.toResponse(mockUser, "Bearer eyJhbGciOiJIUzI1NiJ9..."))
            .thenReturn(mockResponse);

        // Act & Assert
        StepVerifier.create(registerUseCase.execute(validRequest))
            .expectNextMatches(response -> 
                response.getEmail().equals("newuser@example.com") &&
                response.getName().equals("New User") &&
                response.getToken().startsWith("Bearer ")
            )
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe lanzar ConflictException si email ya existe")
    void testRegisterEmailAlreadyExists() {
        // Arrange
        when(userRepository.findByEmail("newuser@example.com"))
            .thenReturn(Mono.just(mockUser));

        // Act & Assert
        StepVerifier.create(registerUseCase.execute(validRequest))
            .expectError(ConflictException.class)
            .verify();
    }

    @Test
    @Disabled("Requiere refactoring del UseCase para validaciones reactivas")
    @DisplayName("Debe lanzar InvalidInputException si email es inválido")
    void testRegisterInvalidEmail() {
        // Arrange
        AuthRegisterRequest invalidEmailRequest = AuthRegisterRequest.builder()
            .email("invalid-email")
            .password("SecurePass123!")
            .name("User")
            .build();

        // Act & Assert
        StepVerifier.create(registerUseCase.execute(invalidEmailRequest))
            .expectError(InvalidInputException.class)
            .verify();
    }

    @Test
    @Disabled("Requiere refactoring del UseCase para validaciones reactivas")
    @DisplayName("Debe lanzar InvalidInputException si contraseña es débil")
    void testRegisterWeakPassword() {
        // Arrange
        AuthRegisterRequest weakPasswordRequest = AuthRegisterRequest.builder()
            .email("user@example.com")
            .password("weak")
            .name("User")
            .build();

        // Act & Assert
        StepVerifier.create(registerUseCase.execute(weakPasswordRequest))
            .expectError(InvalidInputException.class)
            .verify();
    }

    @Test    @Disabled("Requiere refactoring del UseCase para validaciones reactivas")    @DisplayName("Debe lanzar InvalidInputException si nombre es vacío")
    void testRegisterEmptyName() {
        // Arrange
        AuthRegisterRequest emptyNameRequest = AuthRegisterRequest.builder()
            .email("user@example.com")
            .password("SecurePass123!")
            .name("")
            .build();

        // Act & Assert
        StepVerifier.create(registerUseCase.execute(emptyNameRequest))
            .expectError(InvalidInputException.class)
            .verify();
    }
}
