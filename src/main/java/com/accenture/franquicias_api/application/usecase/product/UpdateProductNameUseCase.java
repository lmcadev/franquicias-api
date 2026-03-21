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
 * Caso de uso para actualizar el nombre de un producto.
 *
 * <p>
 * Realiza las siguientes operaciones:
 * <ul>
 *   <li>Valida el nuevo nombre del producto</li>
 *   <li>Busca el producto en la base de datos</li>
 *   <li>Actualiza solo el nombre del producto</li>
 *   <li>Persiste los cambios</li>
 *   <li>Retorna la respuesta con datos actualizados</li>
 * </ul>
 * </p>
 *
 * <p>
 * Lanza excepciones:
 * <ul>
 *   <li>{@link ResourceNotFoundException} si el producto no existe</li>
 * </ul>
 * </p>
 *
 * @see ProductUpdateNameRequest
 * @see ProductResponse
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
