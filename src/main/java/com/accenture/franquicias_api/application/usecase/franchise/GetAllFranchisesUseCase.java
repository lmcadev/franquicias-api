package com.accenture.franquicias_api.application.usecase.franchise;

import com.accenture.franquicias_api.application.dto.response.franchise.FranchiseResponse;
import com.accenture.franquicias_api.application.mapper.franchise.FranchiseMapper;
import com.accenture.franquicias_api.application.utils.ValidationUtils;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * UseCase para obtener todas las franquicias con paginación
 */
@Component
@RequiredArgsConstructor
public class GetAllFranchisesUseCase {
    
    private final FranchiseRepository franchiseRepository;
    private final FranchiseMapper franchiseMapper;
    
    public Flux<FranchiseResponse> execute(Pageable pageable) {
        // Validaciones
        ValidationUtils.validatePageNumber(pageable.getPageNumber());
        ValidationUtils.validatePageSize(pageable.getPageSize());
        
        // Obtener todas las franquicias con paginación
        return franchiseRepository.findAll(pageable)
            .map(franchiseMapper::toResponse);
    }
}
