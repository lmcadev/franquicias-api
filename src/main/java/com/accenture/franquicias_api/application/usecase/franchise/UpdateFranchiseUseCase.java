package com.accenture.franquicias_api.application.usecase.franchise;

import com.accenture.franquicias_api.application.dto.request.franchise.FranchiseUpdateRequest;
import com.accenture.franquicias_api.application.dto.response.franchise.FranchiseResponse;
import com.accenture.franquicias_api.application.mapper.franchise.FranchiseMapper;
import com.accenture.franquicias_api.application.utils.ValidationUtils;
import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Caso de uso para actualizar una franquicia existente.
 *
 * <p>
 * Realiza las siguientes operaciones:
 * <ul>
 *   <li>Valida el nombre actualizado</li>
 *   <li>Verifica que la franquicia existe</li>
 *   <li>Convierte el DTO de solicitud a entidad de dominio</li>
 *   <li>Persiste los cambios en la base de datos</li>
 *   <li>Retorna la respuesta con datos actualizados</li>
 * </ul>
 * </p>
 *
 * <p>
 * Lanza excepciones:
 * <ul>
 *   <li>{@link ResourceNotFoundException} si la franquicia no existe</li>
 * </ul>
 * </p>
 *
 * @see FranchiseUpdateRequest
 * @see FranchiseResponse
 */
@Component
@RequiredArgsConstructor
public class UpdateFranchiseUseCase {
    
    private final FranchiseRepository franchiseRepository;
    private final FranchiseMapper franchiseMapper;
    
    public Mono<FranchiseResponse> execute(Long franchiseId, FranchiseUpdateRequest request) {
        // Validaciones
        ValidationUtils.validateName(request.getName(), "name");
        
        // Verificar que la franquicia existe
        return franchiseRepository.findById(franchiseId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia", String.valueOf(franchiseId))))
            .flatMap(existingFranchise -> {
                // Actualizar campos
                existingFranchise.setName(request.getName());
                existingFranchise.setDescription(request.getDescription());
                
                // Guardar cambios
                return franchiseRepository.update(existingFranchise);
            })
            .map(franchiseMapper::toResponse);
    }
}
