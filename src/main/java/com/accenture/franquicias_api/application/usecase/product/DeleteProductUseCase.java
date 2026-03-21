package com.accenture.franquicias_api.application.usecase.product;

import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Caso de uso para eliminar un producto.
 *
 * <p>
 * Realiza las siguientes operaciones:
 * <ul>
 *   <li>Busca el producto a eliminar</li>
 *   <li>Implementa eliminación suave (soft delete)</li>
 *   <li>Establece el timestamp deleted_at sin eliminar físicamente</li>
 *   <li>Retorna confirmación de eliminación</li>
 * </ul>
 * </p>
 *
 * <p>
 * Lanza excepciones:
 * <ul>
 *   <li>{@link ResourceNotFoundException} si el producto no existe</li>
 * </ul>
 * </p>
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
