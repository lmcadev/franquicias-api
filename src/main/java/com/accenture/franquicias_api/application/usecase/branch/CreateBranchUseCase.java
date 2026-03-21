package com.accenture.franquicias_api.application.usecase.branch;

import com.accenture.franquicias_api.application.dto.request.branch.BranchCreateRequest;
import com.accenture.franquicias_api.application.dto.response.branch.BranchResponse;
import com.accenture.franquicias_api.application.mapper.branch.BranchMapper;
import com.accenture.franquicias_api.application.utils.ValidationUtils;
import com.accenture.franquicias_api.domain.entity.branch.Branch;
import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.branch.BranchRepository;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Caso de uso para crear una nueva sucursal dentro de una franquicia.
 *
 * <p>
 * Realiza las siguientes operaciones:
 * <ul>
 *   <li>Valida el nombre de la sucursal</li>
 *   <li>Verifica que la franquicia padre existe</li>
 *   <li>Convierte el DTO de solicitud a entidad de dominio</li>
 *   <li>Asigna el ID de la franquicia a la sucursal</li>
 *   <li>Persiste la sucursal en la base de datos</li>
 *   <li>Retorna la respuesta con datos de la sucursal creada</li>
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
 * @see BranchCreateRequest
 * @see BranchResponse
 * @see Branch
 */
@Component
@RequiredArgsConstructor
public class CreateBranchUseCase {
    
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;
    private final BranchMapper branchMapper;
    
    public Mono<BranchResponse> execute(Long franchiseId, BranchCreateRequest request) {
        // Validaciones
        ValidationUtils.validateName(request.getName(), "name");
        
        // Verificar que la franquicia existe
        return franchiseRepository.findById(franchiseId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia", String.valueOf(franchiseId))))
            .flatMap(franchise -> {
                // Mapear DTO a entidad de dominio
                Branch branch = branchMapper.toDomain(request);
                branch.setFranchiseId(franchiseId);
                
                // Guardar sucursal
                return branchRepository.save(branch);
            })
            .map(branchMapper::toResponse);
    }
}
