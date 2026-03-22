package com.accenture.franquicias_api.presentation.controller.product;

import com.accenture.franquicias_api.application.dto.response.product.ProductResponse;
import com.accenture.franquicias_api.application.usecase.product.AddProductUseCase;
import com.accenture.franquicias_api.application.usecase.product.DeleteProductUseCase;
import com.accenture.franquicias_api.application.usecase.product.GetMaxStockProductByFranchiseUseCase;
import com.accenture.franquicias_api.application.usecase.product.GetMaxStockProductsByFranchiseUseCase;
import com.accenture.franquicias_api.application.usecase.product.GetProductsByBranchUseCase;
import com.accenture.franquicias_api.application.usecase.product.UpdateProductNameUseCase;
import com.accenture.franquicias_api.application.usecase.product.UpdateProductStockUseCase;
import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductController Tests")
class ProductControllerTest {

    @Mock
    private AddProductUseCase addProductUseCase;

    @Mock
    private UpdateProductNameUseCase updateProductNameUseCase;

    @Mock
    private UpdateProductStockUseCase updateProductStockUseCase;

    @Mock
    private DeleteProductUseCase deleteProductUseCase;

    @Mock
    private GetProductsByBranchUseCase getProductsByBranchUseCase;

    @Mock
    private GetMaxStockProductByFranchiseUseCase getMaxStockProductByFranchiseUseCase;

    @Mock
    private GetMaxStockProductsByFranchiseUseCase getMaxStockProductsByFranchiseUseCase;

    @InjectMocks
    private ProductController productController;

    private ProductResponse response1;
    private ProductResponse response2;

    @BeforeEach
    void setUp() {
        response1 = ProductResponse.builder()
            .id(10L)
            .branchId(1L)
            .name("Laptop")
            .stock(30)
            .build();

        response2 = ProductResponse.builder()
            .id(20L)
            .branchId(2L)
            .name("Mouse")
            .stock(40)
            .build();
    }

    @Test
    @DisplayName("Debe retornar listado de maximos por sucursal")
    void getMaxStockProductsByFranchiseSuccess() {
        when(getMaxStockProductsByFranchiseUseCase.execute(1L))
            .thenReturn(Flux.just(response1, response2));

        StepVerifier.create(productController.getMaxStockProductsByFranchise(1L))
            .expectNextMatches(product ->
                product.getId().equals(10L)
                    && product.getBranchId().equals(1L)
                    && product.getStock().equals(30)
            )
            .expectNextMatches(product ->
                product.getId().equals(20L)
                    && product.getBranchId().equals(2L)
                    && product.getStock().equals(40)
            )
            .verifyComplete();
    }

    @Test
    @DisplayName("Debe propagar error cuando la franquicia no existe")
    void getMaxStockProductsByFranchiseNotFound() {
        when(getMaxStockProductsByFranchiseUseCase.execute(999L))
            .thenReturn(Flux.error(new ResourceNotFoundException("Franquicia", "999")));

        StepVerifier.create(productController.getMaxStockProductsByFranchise(999L))
            .expectError(ResourceNotFoundException.class)
            .verify();
    }
}
