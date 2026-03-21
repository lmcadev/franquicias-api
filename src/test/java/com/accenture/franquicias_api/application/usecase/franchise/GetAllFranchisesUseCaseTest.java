package com.accenture.franquicias_api.application.usecase.franchise;

import com.accenture.franquicias_api.application.dto.response.franchise.FranchiseResponse;
import com.accenture.franquicias_api.application.mapper.franchise.FranchiseMapper;
import com.accenture.franquicias_api.domain.entity.franchise.Franchise;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para GetAllFranchisesUseCase usando JUnit 5 + Mockito + StepVerifier
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetAllFranchisesUseCase Tests")
class GetAllFranchisesUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private FranchiseMapper franchiseMapper;

    @InjectMocks
    private GetAllFranchisesUseCase getAllFranchisesUseCase;

    private Franchise mockFranchise1;
    private Franchise mockFranchise2;
    private FranchiseResponse mockResponse1;
    private FranchiseResponse mockResponse2;

    @BeforeEach
    void setUp() {
        mockFranchise1 = Franchise.builder()
            .name("Franquicia 1")
            .description("Descripción 1")
            .build();

        mockFranchise2 = Franchise.builder()
            .name("Franquicia 2")
            .description("Descripción 2")
            .build();

        mockResponse1 = FranchiseResponse.builder()
            .id(1L)
            .name("Franquicia 1")
            .description("Descripción 1")
            .build();

        mockResponse2 = FranchiseResponse.builder()
            .id(2L)
            .name("Franquicia 2")
            .description("Descripción 2")
            .build();
    }

    @Test
    @DisplayName("Debe obtener todas las franquicias exitosamente")
    void testGetAllFranchisesSuccess() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 20);
        when(franchiseRepository.findAll(pageable))
            .thenReturn(Flux.just(mockFranchise1, mockFranchise2));
        when(franchiseMapper.toResponse(mockFranchise1))
            .thenReturn(mockResponse1);
        when(franchiseMapper.toResponse(mockFranchise2))
            .thenReturn(mockResponse2);

        // Act & Assert
        StepVerifier.create(getAllFranchisesUseCase.execute(pageable))
            .expectNextMatches(response -> response.getId() == 1L && response.getName().equals("Franquicia 1"))
            .expectNextMatches(response -> response.getId() == 2L && response.getName().equals("Franquicia 2"))
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar Flux vacío si no hay franquicias")
    void testGetAllFranchisesEmpty() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 20);
        when(franchiseRepository.findAll(pageable))
            .thenReturn(Flux.empty());

        // Act & Assert
        StepVerifier.create(getAllFranchisesUseCase.execute(pageable))
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe validar parámetros de paginación")
    void testGetAllFranchisesWithValidation() {
        // Arrange
        Pageable validPageable = PageRequest.of(0, 20);
        when(franchiseRepository.findAll(validPageable))
            .thenReturn(Flux.just(mockFranchise1));
        when(franchiseMapper.toResponse(mockFranchise1))
            .thenReturn(mockResponse1);

        // Act & Assert
        StepVerifier.create(getAllFranchisesUseCase.execute(validPageable))
            .expectNextCount(1)
            .verifyComplete();
    }
}
