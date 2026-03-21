package com.accenture.franquicias_api.application.usecase.franchise;

import com.accenture.franquicias_api.application.dto.request.franchise.FranchiseCreateRequest;
import com.accenture.franquicias_api.application.dto.response.franchise.FranchiseResponse;
import com.accenture.franquicias_api.application.mapper.franchise.FranchiseMapper;
import com.accenture.franquicias_api.application.utils.ValidationUtils;
import com.accenture.franquicias_api.domain.entity.franchise.Franchise;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Caso de uso para crear una nueva franquicia.
 *
 * <p>
 * Realiza las siguientes operaciones:
 * <ul>
 *   <li>Valida el nombre de la franquicia</li>
 *   <li>Convierte el DTO de solicitud a entidad de dominio</li>
 *   <li>Asigna el usuario creador (owner) de la franquicia</li>
 *   <li>Persiste la franquicia en la base de datos</li>
 *   <li>Retorna la respuesta con datos de la franquicia creada</li>
 * </ul>
 * </p>
 *
 * <p>
 * Solo usuarios autenticados pueden crear franquicias.
 * </p>
 *
 * @see FranchiseCreateRequest
 * @see FranchiseResponse
 * @see Franchise
 */
@Component
@RequiredArgsConstructor
public class CreateFranchiseUseCase {
    
    private final FranchiseRepository franchiseRepository;
    private final FranchiseMapper franchiseMapper;
    
    public Mono<FranchiseResponse> execute(FranchiseCreateRequest request, Long userId) {
        // Validaciones
        ValidationUtils.validateName(request.getName(), "name");
        
        // Mapear DTO a entidad de dominio
        Franchise franchise = franchiseMapper.toDomain(request);
        franchise.setCreatedBy(userId);
        
        // Guardar franquicia y devolver exactamente la entidad recién creada.
        // Fallback: si R2DBC no devuelve ID en el save, resolver por nombre.
        return franchiseRepository.save(franchise)
            .flatMap(saved -> {
                if (saved.getId() != null) {
                    return Mono.just(saved);
                }
                return franchiseRepository.findByName(request.getName())
                    .switchIfEmpty(Mono.error(new IllegalStateException("No se pudo resolver la franquicia creada")));
            })
            .map(franchiseMapper::toResponse);
    }
}
