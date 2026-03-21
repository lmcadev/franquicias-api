package com.accenture.franquicias_api.presentation.controller.franchise;

import com.accenture.franquicias_api.application.dto.request.franchise.FranchiseCreateRequest;
import com.accenture.franquicias_api.application.dto.request.franchise.FranchiseUpdateRequest;
import com.accenture.franquicias_api.application.dto.response.franchise.FranchiseResponse;
import com.accenture.franquicias_api.application.usecase.franchise.CreateFranchiseUseCase;
import com.accenture.franquicias_api.application.usecase.franchise.DeleteFranchiseUseCase;
import com.accenture.franquicias_api.application.usecase.franchise.GetAllFranchisesUseCase;
import com.accenture.franquicias_api.application.usecase.franchise.GetFranchiseByIdUseCase;
import com.accenture.franquicias_api.application.usecase.franchise.UpdateFranchiseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
 * Controller para gestión de franquicias
 */
@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
public class FranchiseController {
    
    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final UpdateFranchiseUseCase updateFranchiseUseCase;
    private final GetAllFranchisesUseCase getAllFranchisesUseCase;
    private final GetFranchiseByIdUseCase getFranchiseByIdUseCase;
    private final DeleteFranchiseUseCase deleteFranchiseUseCase;
    
    /**
     * Crear nueva franquicia
     * POST /api/franchises
     */
    @PostMapping
    public Mono<ResponseEntity<FranchiseResponse>> create(
            @Valid @RequestBody FranchiseCreateRequest request) {
        Long userId = getUserIdFromSecurityContext();
        return createFranchiseUseCase.execute(request, userId)
            .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }
    
    /**
     * Actualizar franquicia
     * PUT /api/franchises/{id}
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FranchiseResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody FranchiseUpdateRequest request) {
        return updateFranchiseUseCase.execute(id, request)
            .map(response -> ResponseEntity.ok(response));
    }
    
    /**
     * Obtener todas las franquicias con paginación
     * GET /api/franchises?page=0&size=20
     */
    @GetMapping
    public Flux<FranchiseResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return getAllFranchisesUseCase.execute(pageable);
    }
    
    /**
     * Obtener franquicia por ID
     * GET /api/franchises/{id}
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FranchiseResponse>> getById(@PathVariable Long id) {
        return getFranchiseByIdUseCase.execute(id)
            .map(response -> ResponseEntity.ok(response));
    }
    
    /**
     * Eliminar franquicia
     * DELETE /api/franchises/{id}
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return deleteFranchiseUseCase.execute(id)
            .map(v -> ResponseEntity.noContent().<Void>build());
    }
    
    /**
     * Extrae el userId del SecurityContext.
     * Lanza exception si no está autenticado.
     */
    private Long getUserIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object details = authentication.getDetails();
            if (details instanceof Long) {
                return (Long) details;
            }
        }
        throw new IllegalStateException("Usuario no autenticado o userId no encontrado");
    }
}
