package com.accenture.franquicias_api.presentation.controller.branch;

import com.accenture.franquicias_api.application.dto.request.branch.BranchCreateRequest;
import com.accenture.franquicias_api.application.dto.request.branch.BranchUpdateRequest;
import com.accenture.franquicias_api.application.dto.response.branch.BranchResponse;
import com.accenture.franquicias_api.application.usecase.branch.CreateBranchUseCase;
import com.accenture.franquicias_api.application.usecase.branch.DeleteBranchUseCase;
import com.accenture.franquicias_api.application.usecase.branch.GetBranchesByFranchiseUseCase;
import com.accenture.franquicias_api.application.usecase.branch.UpdateBranchUseCase;
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
    public Mono<ResponseEntity<BranchResponse>> create(
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
    public Mono<ResponseEntity<BranchResponse>> update(
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
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return deleteBranchUseCase.execute(id)
            .map(v -> ResponseEntity.noContent().<Void>build());
    }
    
    /**
     * Obtener todas las sucursales de una franquicia con paginación
     * GET /api/franchises/{franchiseId}/branches?page=0&size=20
     */
    @GetMapping("/franchises/{franchiseId}/branches")
    public Flux<BranchResponse> getByFranchise(
            @PathVariable Long franchiseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return getBranchesByFranchiseUseCase.execute(franchiseId, pageable);
    }
}
