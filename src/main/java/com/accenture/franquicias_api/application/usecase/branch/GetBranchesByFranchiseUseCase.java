package com.accenture.franquicias_api.application.usecase.branch;

import com.accenture.franquicias_api.application.dto.response.branch.BranchResponse;
import com.accenture.franquicias_api.application.mapper.branch.BranchMapper;
import com.accenture.franquicias_api.application.utils.ValidationUtils;
import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.branch.BranchRepository;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * UseCase para obtener todas las sucursales de una franquicia con paginación
 */
@Component
@RequiredArgsConstructor
public class GetBranchesByFranchiseUseCase {
    
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;
    private final BranchMapper branchMapper;
    
    public Flux<BranchResponse> execute(Long franchiseId, Pageable pageable) {
        // Validaciones
        ValidationUtils.validatePageNumber(pageable.getPageNumber());
        ValidationUtils.validatePageSize(pageable.getPageSize());
        
        // Verificar que la franquicia existe
        return franchiseRepository.findById(franchiseId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia", String.valueOf(franchiseId))))
            .flatMapMany(franchise -> branchRepository.findByFranchiseId(franchiseId, pageable))
            .map(branchMapper::toResponse);
    }
}
