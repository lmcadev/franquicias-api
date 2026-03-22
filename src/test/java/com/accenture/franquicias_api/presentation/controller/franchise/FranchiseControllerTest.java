package com.accenture.franquicias_api.presentation.controller.franchise;

import com.accenture.franquicias_api.application.dto.request.franchise.FranchiseCreateRequest;
import com.accenture.franquicias_api.application.dto.response.franchise.FranchiseResponse;
import com.accenture.franquicias_api.application.usecase.franchise.CreateFranchiseUseCase;
import com.accenture.franquicias_api.application.usecase.franchise.DeleteFranchiseUseCase;
import com.accenture.franquicias_api.application.usecase.franchise.GetAllFranchisesUseCase;
import com.accenture.franquicias_api.application.usecase.franchise.GetFranchiseByIdUseCase;
import com.accenture.franquicias_api.application.usecase.franchise.UpdateFranchiseUseCase;
import com.accenture.franquicias_api.application.dto.request.franchise.FranchiseUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para FranchiseController usando JUnit  + Mockito + StepVerifier
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FranchiseController Tests")
class FranchiseControllerTest {

    @Mock
    private CreateFranchiseUseCase createFranchiseUseCase;

    @Mock
    private UpdateFranchiseUseCase updateFranchiseUseCase;

    @Mock
    private GetAllFranchisesUseCase getAllFranchisesUseCase;

    @Mock
    private GetFranchiseByIdUseCase getFranchiseByIdUseCase;

    @Mock
    private DeleteFranchiseUseCase deleteFranchiseUseCase;

    @InjectMocks
    private FranchiseController franchiseController;

    private FranchiseCreateRequest createRequest;
    private FranchiseUpdateRequest updateRequest;
    private FranchiseResponse franchiseResponse;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        // Setup authentication for reactive security context
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken("user@example.com", null, null);
        auth.setDetails(1L);
        authentication = auth;

        // Mantener también el contexto imperativo por compatibilidad con otros tests.
        SecurityContextHolder.getContext().setAuthentication(auth);

        createRequest = FranchiseCreateRequest.builder()
            .name("Nueva Franquicia")
            .description("Descripción")
            .build();

        updateRequest = FranchiseUpdateRequest.builder()
            .name("Franquicia Actualizada")
            .description("Nueva descripción")
            .build();

        franchiseResponse = FranchiseResponse.builder()
            .id(1L)
            .name("Nueva Franquicia")
            .description("Descripción")
            .build();
    }

    @Test
    @DisplayName("Debe crear franquicia y retornar 201 Created")
    void testCreateFranchiseSuccess() {
        // Arrange
        when(createFranchiseUseCase.execute(createRequest, 1L))
            .thenReturn(Mono.just(franchiseResponse));

        // Act
        Mono<ResponseEntity<FranchiseResponse>> response = franchiseController.create(createRequest);

        // Assert
        StepVerifier.create(response.contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)))
            .expectNextMatches(entity -> 
                entity.getStatusCode() == HttpStatus.CREATED &&
                entity.getBody().getId() == 1L &&
                entity.getBody().getName().equals("Nueva Franquicia")
            )
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe actualizar franquicia y retornar 200 OK")
    void testUpdateFranchiseSuccess() {
        // Arrange
        FranchiseResponse updatedResponse = FranchiseResponse.builder()
            .id(1L)
            .name("Franquicia Actualizada")
            .description("Nueva descripción")
            .build();

        when(updateFranchiseUseCase.execute(1L, updateRequest))
            .thenReturn(Mono.just(updatedResponse));

        // Act
        Mono<ResponseEntity<FranchiseResponse>> response = franchiseController.update(1L, updateRequest);

        // Assert
        StepVerifier.create(response)
            .expectNextMatches(entity -> 
                entity.getStatusCode() == HttpStatus.OK &&
                entity.getBody().getName().equals("Franquicia Actualizada")
            )
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener todas las franquicias y retornar 200 OK")
    void testGetAllFranchisesSuccess() {
        // Arrange
        when(getAllFranchisesUseCase.execute(any()))
            .thenReturn(Flux.just(franchiseResponse));

        // Act
        Flux<FranchiseResponse> response = franchiseController.getAll(0, 20);

        // Assert
        StepVerifier.create(response)
            .expectNextMatches(franchise -> 
                franchise.getId() == 1L &&
                franchise.getName().equals("Nueva Franquicia")
            )
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener franquicia por ID y retornar 200 OK")
    void testGetFranchiseByIdSuccess() {
        // Arrange
        when(getFranchiseByIdUseCase.execute(1L))
            .thenReturn(Mono.just(franchiseResponse));

        // Act
        Mono<ResponseEntity<FranchiseResponse>> response = franchiseController.getById(1L);

        // Assert
        StepVerifier.create(response)
            .expectNextMatches(entity -> 
                entity.getStatusCode() == HttpStatus.OK &&
                entity.getBody().getId() == 1L
            )
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe eliminar franquicia y retornar 204 No Content")
    void testDeleteFranchiseSuccess() {
        // Arrange
        when(deleteFranchiseUseCase.execute(1L))
            .thenReturn(Mono.empty());

        // Act
        Mono<ResponseEntity<Void>> response = franchiseController.delete(1L);

        // Assert
        StepVerifier.create(response)
            .verifyComplete();
    }
}
