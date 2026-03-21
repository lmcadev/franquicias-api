package com.accenture.franquicias_api.application.usecase.branch;

import com.accenture.franquicias_api.application.dto.request.branch.BranchUpdateRequest;
import com.accenture.franquicias_api.application.dto.response.branch.BranchResponse;
import com.accenture.franquicias_api.application.mapper.branch.BranchMapper;
import com.accenture.franquicias_api.application.utils.ValidationUtils;
import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.branch.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * UseCase para actualizar una sucursal existente
 */
@Component
@RequiredArgsConstructor
public class UpdateBranchUseCase {
    
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    
    public Mono<BranchResponse> execute(Long branchId, BranchUpdateRequest request) {
        // Validaciones
        ValidationUtils.validateName(request.getName(), "name");
        
        // Verificar que la sucursal existe
        return branchRepository.findById(branchId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sucursal", String.valueOf(branchId))))
            .flatMap(existingBranch -> {
                // Actualizar campos
                existingBranch.setName(request.getName());
                existingBranch.setAddress(request.getAddress());
                
                // Guardar cambios
                return branchRepository.update(existingBranch);
            })
            .map(branchMapper::toResponse);
    }
}
