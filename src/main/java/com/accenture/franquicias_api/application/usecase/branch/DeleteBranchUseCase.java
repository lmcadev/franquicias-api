package com.accenture.franquicias_api.application.usecase.branch;

import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.branch.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * UseCase para eliminar (soft delete) una sucursal
 */
@Component
@RequiredArgsConstructor
public class DeleteBranchUseCase {
    
    private final BranchRepository branchRepository;
    
    public Mono<Void> execute(Long branchId) {
        // Verificar que la sucursal existe
        return branchRepository.findById(branchId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sucursal", String.valueOf(branchId))))
            .flatMap(branch -> branchRepository.delete(branchId));
    }
}
