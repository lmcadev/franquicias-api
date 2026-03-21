package com.accenture.franquicias_api.application.usecase.product;

import com.accenture.franquicias_api.application.dto.request.product.ProductStockUpdateRequest;
import com.accenture.franquicias_api.application.dto.response.product.ProductResponse;
import com.accenture.franquicias_api.application.mapper.product.ProductMapper;
import com.accenture.franquicias_api.application.utils.ValidationUtils;
import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * UseCase para actualizar el stock de un producto
 */
@Component
@RequiredArgsConstructor
public class UpdateProductStockUseCase {
    
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    public Mono<ProductResponse> execute(Long productId, ProductStockUpdateRequest request) {
        // Validaciones
        ValidationUtils.validateStock(request.getStock());
        
        // Verificar que el producto existe
        return productRepository.findById(productId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto", String.valueOf(productId))))
            .flatMap(existingProduct -> {
                // Actualizar stock
                existingProduct.setStock(request.getStock());
                
                // Guardar cambios
                return productRepository.update(existingProduct);
            })
            .map(productMapper::toResponse);
    }
}
