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
 * Caso de uso para obtener el producto con máximo stock en una franquicia.
 *
 * <p>
 * Realiza las siguientes operaciones:
 * <ul>
 *   <li>Verifica que la franquicia existe</li>
 *   <li>Busca el producto con mayor stock en todas las sucursales</li>
 *   <li>Enriquece la respuesta con datos de la franquicia y sucursal</li>
 *   <li>Convierte a DTO de respuesta especializada</li>
 * </ul>
 * </p>
 *
 * <p>
 * Lanza excepciones:
 * <ul>
 *   <li>{@link ResourceNotFoundException} si la franquicia no existe o no hay productos</li>
 * </ul>
 * </p>
 *
 * @see ProductResponse
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
