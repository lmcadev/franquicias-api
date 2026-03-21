package com.accenture.franquicias_api.presentation.controller.franchise;

import com.accenture.franquicias_api.application.dto.request.franchise.FranchiseCreateRequest;
import com.accenture.franquicias_api.application.dto.request.franchise.FranchiseUpdateRequest;
import com.accenture.franquicias_api.application.dto.response.franchise.FranchiseResponse;
import com.accenture.franquicias_api.application.usecase.franchise.CreateFranchiseUseCase;
import com.accenture.franquicias_api.application.usecase.franchise.DeleteFranchiseUseCase;
import com.accenture.franquicias_api.application.usecase.franchise.GetAllFranchisesUseCase;
import com.accenture.franquicias_api.application.usecase.franchise.GetFranchiseByIdUseCase;
import com.accenture.franquicias_api.application.usecase.franchise.UpdateFranchiseUseCase;
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
@Tag(name = "Franquicias", description = "Endpoints para gestionar franquicias (CRUD con autenticación)")
@SecurityRequirement(name = "Authorization")
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
    @Operation(
        summary = "Crear nueva franquicia",
        description = "Crea una nueva franquicia con el usuario autenticado como propietario"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Franquicia creada exitosamente",
            content = @Content(schema = @Schema(implementation = FranchiseResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "409", description = "Nombre duplicado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Operation(
        summary = "Actualizar franquicia",
        description = "Actualiza nombre y descripción de una franquicia existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Franquicia actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = FranchiseResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "404", description = "Franquicia no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<FranchiseResponse>> update(
            @Parameter(description = "ID de la franquicia a actualizar", required = true)
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
    @Operation(
        summary = "Obtener todas las franquicias",
        description = "Retorna listado paginado de todas las franquicias"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Listado de franquicias obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = FranchiseResponse.class))
        ),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Flux<FranchiseResponse> getAll(
            @Parameter(description = "Número de página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return getAllFranchisesUseCase.execute(pageable);
    }
    
    /**
     * Obtener franquicia por ID
     * GET /api/franchises/{id}
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener franquicia por ID",
        description = "Retorna los detalles de una franquicia específica"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Franquicia obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = FranchiseResponse.class))
        ),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "404", description = "Franquicia no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<FranchiseResponse>> getById(
            @Parameter(description = "ID de la franquicia", required = true, example = "1")
            @PathVariable Long id) {
        return getFranchiseByIdUseCase.execute(id)
            .map(response -> ResponseEntity.ok(response));
    }
    
    /**
     * Eliminar franquicia
     * DELETE /api/franchises/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar franquicia",
        description = "Elimina una franquicia existente (soft delete)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Franquicia eliminada exitosamente"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "404", description = "Franquicia no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "ID de la franquicia a eliminar", required = true, example = "1")
            @PathVariable Long id) {
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
