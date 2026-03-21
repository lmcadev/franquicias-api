package com.accenture.franquicias_api.application.usecase.product;

import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * UseCase para eliminar (soft delete) un producto
 */
@Component
@RequiredArgsConstructor
public class DeleteProductUseCase {
    
    private final ProductRepository productRepository;
    
    public Mono<Void> execute(Long productId) {
        // Verificar que el producto existe
        return productRepository.findById(productId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto", String.valueOf(productId))))
            .flatMap(product -> productRepository.delete(productId));
    }
}
