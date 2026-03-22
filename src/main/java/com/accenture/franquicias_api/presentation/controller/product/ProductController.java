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
import com.accenture.franquicias_api.presentation.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Productos", description = "Endpoints para gestionar productos de sucursales (CRUD con autenticación)")
@SecurityRequirement(name = "Authorization")
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
    @Operation(
        summary = "Agregar nuevo producto",
        description = "Crea un nuevo producto en una sucursal con stock y precio inicial"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Producto agregado exitosamente",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Usuario no autenticado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Sucursal no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public Mono<ResponseEntity<ProductResponse>> add(
            @Parameter(description = "ID de la sucursal", required = true, example = "1")
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
    @Operation(
        summary = "Actualizar nombre del producto",
        description = "Actualiza el nombre de un producto existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Nombre actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Usuario no autenticado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public Mono<ResponseEntity<ProductResponse>> updateName(
            @Parameter(description = "ID del producto a actualizar", required = true, example = "1")
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
    @Operation(
        summary = "Actualizar stock del producto",
        description = "Actualiza la cantidad de stock de un producto existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Stock actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Usuario no autenticado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public Mono<ResponseEntity<ProductResponse>> updateStock(
            @Parameter(description = "ID del producto a actualizar", required = true, example = "1")
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
    @Operation(
        summary = "Eliminar producto",
        description = "Elimina un producto existente (soft delete)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        @ApiResponse(
            responseCode = "401",
            description = "Usuario no autenticado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "ID del producto a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        return deleteProductUseCase.execute(id)
            .map(v -> ResponseEntity.noContent().<Void>build());
    }
    
    /**
     * Obtener todos los productos de una sucursal con paginación
     * GET /api/branches/{branchId}/products?page=0&size=20
     */
    @GetMapping("/branches/{branchId}/products")
    @Operation(
        summary = "Obtener productos por sucursal",
        description = "Retorna listado paginado de productos de una sucursal"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Listado de productos obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Usuario no autenticado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Sucursal no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public Flux<ProductResponse> getByBranch(
            @Parameter(description = "ID de la sucursal", required = true, example = "1")
            @PathVariable Long branchId,
            @Parameter(description = "Número de página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return getProductsByBranchUseCase.execute(branchId, pageable);
    }
    
    /**
     * Obtener producto con mayor stock en una franquicia
     * GET /api/franchises/{franchiseId}/max-stock-product
     */
    @GetMapping("/franchises/{franchiseId}/max-stock-product")
    @Operation(
        summary = "Obtener producto con mayor stock",
        description = "Retorna el producto con mayor stock en una franquicia"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Usuario no autenticado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Franquicia no encontrada o sin productos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public Mono<ResponseEntity<ProductResponse>> getMaxStockByFranchise(
            @Parameter(description = "ID de la franquicia", required = true, example = "1")
            @PathVariable Long franchiseId) {
        return getMaxStockProductByFranchiseUseCase.execute(franchiseId)
            .map(response -> ResponseEntity.ok(response));
    }
}
