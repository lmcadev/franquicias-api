package com.accenture.franquicias_api.application.usecase.product;

import com.accenture.franquicias_api.application.dto.response.product.ProductResponse;
import com.accenture.franquicias_api.application.mapper.product.ProductMapper;
import com.accenture.franquicias_api.domain.entity.franchise.Franchise;
import com.accenture.franquicias_api.domain.entity.product.Product;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import com.accenture.franquicias_api.domain.repository.product.ProductRepository;
import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetMaxStockProductsByFranchiseUseCase Tests")
class GetMaxStockProductsByFranchiseUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private GetMaxStockProductsByFranchiseUseCase useCase;

    private Franchise franchise;
    private Product productBranch1;
    private Product productBranch2;
    private ProductResponse responseBranch1;
    private ProductResponse responseBranch2;

    @BeforeEach
    void setUp() {
        franchise = Franchise.builder()
            .name("Franquicia Centro")
            .description("Prueba")
            .build();
        franchise.setId(1L);

        productBranch1 = new Product();
        productBranch1.setId(10L);
        productBranch1.setBranchId(100L);
        productBranch1.setName("Producto A");
        productBranch1.setStock(90);

        productBranch2 = new Product();
        productBranch2.setId(11L);
        productBranch2.setBranchId(101L);
        productBranch2.setName("Producto B");
        productBranch2.setStock(120);

        responseBranch1 = ProductResponse.builder()
            .id(10L)
            .branchId(100L)
            .name("Producto A")
            .stock(90)
            .build();

        responseBranch2 = ProductResponse.builder()
            .id(11L)
            .branchId(101L)
            .name("Producto B")
            .stock(120)
            .build();
    }

    @Test
    @DisplayName("Debe retornar un producto maximo por sucursal")
    void executeSuccess() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(franchise));
        when(productRepository.findMaxStockProductsByFranchise(1L))
            .thenReturn(Flux.just(productBranch1, productBranch2));
        when(productMapper.toResponse(productBranch1)).thenReturn(responseBranch1);
        when(productMapper.toResponse(productBranch2)).thenReturn(responseBranch2);

        StepVerifier.create(useCase.execute(1L))
            .expectNextMatches(product ->
                product.getBranchId().equals(100L)
                    && product.getId().equals(10L)
                    && product.getStock().equals(90)
            )
            .expectNextMatches(product ->
                product.getBranchId().equals(101L)
                    && product.getId().equals(11L)
                    && product.getStock().equals(120)
            )
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando la franquicia no existe")
    void executeFranchiseNotFound() {
        when(franchiseRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(999L))
            .expectError(ResourceNotFoundException.class)
            .verify();
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando no hay productos en la franquicia")
    void executeNoProductsFound() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(franchise));
        when(productRepository.findMaxStockProductsByFranchise(1L)).thenReturn(Flux.empty());

        StepVerifier.create(useCase.execute(1L))
            .expectError(ResourceNotFoundException.class)
            .verify();
    }
}
