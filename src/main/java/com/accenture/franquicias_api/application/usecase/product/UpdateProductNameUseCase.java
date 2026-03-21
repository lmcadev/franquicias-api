package com.accenture.franquicias_api.application.usecase.product;

import com.accenture.franquicias_api.application.dto.request.product.ProductUpdateNameRequest;
import com.accenture.franquicias_api.application.dto.response.product.ProductResponse;
import com.accenture.franquicias_api.application.mapper.product.ProductMapper;
import com.accenture.franquicias_api.application.utils.ValidationUtils;
import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * UseCase para actualizar el nombre de un producto
 */
@Component
@RequiredArgsConstructor
public class UpdateProductNameUseCase {
    
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    public Mono<ProductResponse> execute(Long productId, ProductUpdateNameRequest request) {
        // Validaciones
        ValidationUtils.validateName(request.getName(), "name");
        
        // Verificar que el producto existe
        return productRepository.findById(productId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto", String.valueOf(productId))))
            .flatMap(existingProduct -> {
                // Actualizar nombre
                existingProduct.setName(request.getName());
                
                // Guardar cambios
                return productRepository.update(existingProduct);
            })
            .map(productMapper::toResponse);
    }
}
