package com.accenture.franquicias_api.application.usecase.franchise;

import com.accenture.franquicias_api.application.dto.response.franchise.FranchiseResponse;
import com.accenture.franquicias_api.application.mapper.franchise.FranchiseMapper;
import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * UseCase para obtener una franquicia por ID
 */
@Component
@RequiredArgsConstructor
public class GetFranchiseByIdUseCase {
    
    private final FranchiseRepository franchiseRepository;
    private final FranchiseMapper franchiseMapper;
    
    public Mono<FranchiseResponse> execute(Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia", String.valueOf(franchiseId))))
            .map(franchiseMapper::toResponse);
    }
}
