package com.accenture.franquicias_api.application.usecase.product;

import com.accenture.franquicias_api.application.dto.request.product.ProductCreateRequest;
import com.accenture.franquicias_api.application.dto.response.product.ProductResponse;
import com.accenture.franquicias_api.application.mapper.product.ProductMapper;
import com.accenture.franquicias_api.application.utils.ValidationUtils;
import com.accenture.franquicias_api.domain.entity.product.Product;
import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.branch.BranchRepository;
import com.accenture.franquicias_api.domain.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * UseCase para agregar un nuevo producto a una sucursal
 */
@Component
@RequiredArgsConstructor
public class AddProductUseCase {
    
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final ProductMapper productMapper;
    
    public Mono<ProductResponse> execute(Long branchId, ProductCreateRequest request) {
        // Validaciones
        ValidationUtils.validateName(request.getName(), "name");
        ValidationUtils.validateStock(request.getStock());
        
        // Verificar que la sucursal existe
        return branchRepository.findById(branchId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sucursal", String.valueOf(branchId))))
            .flatMap(branch -> {
                // Mapear DTO a entidad de dominio
                Product product = productMapper.toDomain(request);
                product.setBranchId(branchId);
                
                // Guardar producto
                return productRepository.save(product);
            })
            .map(productMapper::toResponse);
    }
}
