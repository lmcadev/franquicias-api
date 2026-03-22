package com.accenture.franquicias_api.application.usecase.product;

import com.accenture.franquicias_api.application.dto.response.product.ProductResponse;
import com.accenture.franquicias_api.application.mapper.product.ProductMapper;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import com.accenture.franquicias_api.domain.repository.product.ProductRepository;
import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Caso de uso para obtener el producto con mayor stock por cada sucursal
 * de una franquicia.
 */
@Component
@RequiredArgsConstructor
public class GetMaxStockProductsByFranchiseUseCase {

    private final ProductRepository productRepository;
    private final FranchiseRepository franchiseRepository;
    private final ProductMapper productMapper;

    public Flux<ProductResponse> execute(Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia", String.valueOf(franchiseId))))
            .flatMapMany(franchise -> productRepository.findMaxStockProductsByFranchise(franchiseId))
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto", "ninguno encontrado en la franquicia")))
            .map(productMapper::toResponse);
    }
}
