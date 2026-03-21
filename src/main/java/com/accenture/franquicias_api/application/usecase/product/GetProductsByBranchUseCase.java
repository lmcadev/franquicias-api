package com.accenture.franquicias_api.application.usecase.product;

import com.accenture.franquicias_api.application.dto.response.product.ProductResponse;
import com.accenture.franquicias_api.application.mapper.product.ProductMapper;
import com.accenture.franquicias_api.application.utils.ValidationUtils;
import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.branch.BranchRepository;
import com.accenture.franquicias_api.domain.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * UseCase para obtener todos los productos de una sucursal con paginación
 */
@Component
@RequiredArgsConstructor
public class GetProductsByBranchUseCase {
    
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final ProductMapper productMapper;
    
    public Flux<ProductResponse> execute(Long branchId, Pageable pageable) {
        // Validaciones
        ValidationUtils.validatePageNumber(pageable.getPageNumber());
        ValidationUtils.validatePageSize(pageable.getPageSize());
        
        // Verificar que la sucursal existe
        return branchRepository.findById(branchId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sucursal", String.valueOf(branchId))))
            .flatMapMany(branch -> productRepository.findByBranchId(branchId, pageable))
            .map(productMapper::toResponse);
    }
}
