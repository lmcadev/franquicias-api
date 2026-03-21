package com.accenture.franquicias_api.presentation.controller.branch;

import com.accenture.franquicias_api.application.dto.request.branch.BranchCreateRequest;
import com.accenture.franquicias_api.application.dto.request.branch.BranchUpdateRequest;
import com.accenture.franquicias_api.application.dto.response.branch.BranchResponse;
import com.accenture.franquicias_api.application.usecase.branch.CreateBranchUseCase;
import com.accenture.franquicias_api.application.usecase.branch.DeleteBranchUseCase;
import com.accenture.franquicias_api.application.usecase.branch.GetBranchesByFranchiseUseCase;
import com.accenture.franquicias_api.application.usecase.branch.UpdateBranchUseCase;
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
 * Controller para gestión de sucursales
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Sucursales", description = "Endpoints para gestionar sucursales de franquicias (CRUD con autenticación)")
@SecurityRequirement(name = "Authorization")
public class BranchController {
    
    private final CreateBranchUseCase createBranchUseCase;
    private final UpdateBranchUseCase updateBranchUseCase;
    private final DeleteBranchUseCase deleteBranchUseCase;
    private final GetBranchesByFranchiseUseCase getBranchesByFranchiseUseCase;
    
    /**
     * Crear nueva sucursal en una franquicia
     * POST /api/franchises/{franchiseId}/branches
     */
    @PostMapping("/franchises/{franchiseId}/branches")
    @Operation(
        summary = "Crear nueva sucursal",
        description = "Crea una nueva sucursal en una franquicia existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Sucursal creada exitosamente",
            content = @Content(schema = @Schema(implementation = BranchResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "404", description = "Franquicia no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<BranchResponse>> create(
            @Parameter(description = "ID de la franquicia", required = true, example = "1")
            @PathVariable Long franchiseId,
            @Valid @RequestBody BranchCreateRequest request) {
        return createBranchUseCase.execute(franchiseId, request)
            .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }
    
    /**
     * Actualizar sucursal
     * PUT /api/branches/{id}
     */
    @PutMapping("/branches/{id}")
    @Operation(
        summary = "Actualizar sucursal",
        description = "Actualiza nombre y datos de una sucursal existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sucursal actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = BranchResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<BranchResponse>> update(
            @Parameter(description = "ID de la sucursal a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody BranchUpdateRequest request) {
        return updateBranchUseCase.execute(id, request)
            .map(response -> ResponseEntity.ok(response));
    }
    
    /**
     * Eliminar sucursal
     * DELETE /api/branches/{id}
     */
    @DeleteMapping("/branches/{id}")
    @Operation(
        summary = "Eliminar sucursal",
        description = "Elimina una sucursal existente (soft delete)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sucursal eliminada exitosamente"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "ID de la sucursal a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        return deleteBranchUseCase.execute(id)
            .map(v -> ResponseEntity.noContent().<Void>build());
    }
    
    /**
     * Obtener todas las sucursales de una franquicia con paginación
     * GET /api/franchises/{franchiseId}/branches?page=0&size=20
     */
    @GetMapping("/franchises/{franchiseId}/branches")
    @Operation(
        summary = "Obtener sucursales por franquicia",
        description = "Retorna listado paginado de todas las sucursales de una franquicia"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Listado de sucursales obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = BranchResponse.class))
        ),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "404", description = "Franquicia no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Flux<BranchResponse> getByFranchise(
            @Parameter(description = "ID de la franquicia", required = true, example = "1")
            @PathVariable Long franchiseId,
            @Parameter(description = "Número de página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return getBranchesByFranchiseUseCase.execute(franchiseId, pageable);
    }
}
