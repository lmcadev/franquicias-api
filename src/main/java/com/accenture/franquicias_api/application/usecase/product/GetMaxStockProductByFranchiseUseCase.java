package com.accenture.franquicias_api.application.usecase.product;

import com.accenture.franquicias_api.application.dto.response.product.ProductResponse;
import com.accenture.franquicias_api.application.mapper.product.ProductMapper;
import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import com.accenture.franquicias_api.domain.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * UseCase para obtener el producto con mayor stock en una franquicia
 */
@Component
@RequiredArgsConstructor
public class GetMaxStockProductByFranchiseUseCase {
    
    private final ProductRepository productRepository;
    private final FranchiseRepository franchiseRepository;
    private final ProductMapper productMapper;
    
    public Mono<ProductResponse> execute(Long franchiseId) {
        // Verificar que la franquicia existe
        return franchiseRepository.findById(franchiseId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia", String.valueOf(franchiseId))))
            .flatMap(franchise -> productRepository.findMaxStockByFranchise(franchiseId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto", "ninguno encontrado en la franquicia")))
                .map(productMapper::toResponse)
            );
    }
}
