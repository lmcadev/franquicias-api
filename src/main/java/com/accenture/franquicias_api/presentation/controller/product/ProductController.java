package com.accenture.franquicias_api.presentation.controller.product;

import com.accenture.franquicias_api.application.dto.request.product.ProductCreateRequest;
import com.accenture.franquicias_api.application.dto.request.product.ProductStockUpdateRequest;
import com.accenture.franquicias_api.application.dto.request.product.ProductUpdateNameRequest;
import com.accenture.franquicias_api.application.dto.response.product.ProductResponse;
import com.accenture.franquicias_api.application.usecase.product.AddProductUseCase;
import com.accenture.franquicias_api.application.usecase.product.DeleteProductUseCase;
import com.accenture.franquicias_api.application.usecase.product.GetMaxStockProductByFranchiseUseCase;
import com.accenture.franquicias_api.application.usecase.product.GetProductsByBranchUseCase;
import com.accenture.franquicias_api.application.usecase.product.UpdateProductNameUseCase;
import com.accenture.franquicias_api.application.usecase.product.UpdateProductStockUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

/**
 * Controller para gestión de productos
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {
    
    private final AddProductUseCase addProductUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetProductsByBranchUseCase getProductsByBranchUseCase;
    private final GetMaxStockProductByFranchiseUseCase getMaxStockProductByFranchiseUseCase;
    
    /**
     * Agregar nuevo producto a una sucursal
     * POST /api/branches/{branchId}/products
     */
    @PostMapping("/branches/{branchId}/products")
    public Mono<ResponseEntity<ProductResponse>> add(
            @PathVariable Long branchId,
            @Valid @RequestBody ProductCreateRequest request) {
        return addProductUseCase.execute(branchId, request)
            .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }
    
    /**
     * Actualizar nombre del producto
     * PUT /api/products/{id}/name
     */
    @PutMapping("/products/{id}/name")
    public Mono<ResponseEntity<ProductResponse>> updateName(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateNameRequest request) {
        return updateProductNameUseCase.execute(id, request)
            .map(response -> ResponseEntity.ok(response));
    }
    
    /**
     * Actualizar stock del producto
     * PATCH /api/products/{id}/stock
     */
    @PatchMapping("/products/{id}/stock")
    public Mono<ResponseEntity<ProductResponse>> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody ProductStockUpdateRequest request) {
        return updateProductStockUseCase.execute(id, request)
            .map(response -> ResponseEntity.ok(response));
    }
    
    /**
     * Eliminar producto
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/products/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return deleteProductUseCase.execute(id)
            .map(v -> ResponseEntity.noContent().<Void>build());
    }
    
    /**
     * Obtener todos los productos de una sucursal con paginación
     * GET /api/branches/{branchId}/products?page=0&size=20
     */
    @GetMapping("/branches/{branchId}/products")
    public Flux<ProductResponse> getByBranch(
            @PathVariable Long branchId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return getProductsByBranchUseCase.execute(branchId, pageable);
    }
    
    /**
     * Obtener producto con mayor stock en una franquicia
     * GET /api/franchises/{franchiseId}/max-stock-product
     */
    @GetMapping("/franchises/{franchiseId}/max-stock-product")
    public Mono<ResponseEntity<ProductResponse>> getMaxStockByFranchise(
            @PathVariable Long franchiseId) {
        return getMaxStockProductByFranchiseUseCase.execute(franchiseId)
            .map(response -> ResponseEntity.ok(response));
    }
}
