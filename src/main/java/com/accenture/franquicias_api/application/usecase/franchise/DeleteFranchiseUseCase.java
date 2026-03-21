package com.accenture.franquicias_api.application.usecase.franchise;

import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * UseCase para eliminar (soft delete) una franquicia
 */
@Component
@RequiredArgsConstructor
public class DeleteFranchiseUseCase {
    
    private final FranchiseRepository franchiseRepository;
    
    public Mono<Void> execute(Long franchiseId) {
        // Verificar que la franquicia existe
        return franchiseRepository.findById(franchiseId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia", String.valueOf(franchiseId))))
            .flatMap(franchise -> franchiseRepository.delete(franchiseId));
    }
}
