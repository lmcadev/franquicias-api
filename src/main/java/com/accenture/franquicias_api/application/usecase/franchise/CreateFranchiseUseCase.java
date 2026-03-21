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
 * UseCase para crear una nueva franquicia
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
        
        // Guardar franquicia
        return franchiseRepository.save(franchise)
            .map(franchiseMapper::toResponse);
    }
}
