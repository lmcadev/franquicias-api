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
 * Caso de uso para agregar un nuevo producto a una sucursal.
 *
 * <p>
 * Realiza las siguientes operaciones:
 * <ul>
 *   <li>Valida el nombre, stock y precio del producto</li>
 *   <li>Verifica que la sucursal padre existe</li>
 *   <li>Convierte el DTO de solicitud a entidad de dominio</li>
 *   <li>Asigna el ID de la sucursal al producto</li>
 *   <li>Persiste el producto en la base de datos</li>
 *   <li>Retorna la respuesta con datos del producto creado</li>
 * </ul>
 * </p>
 *
 * <p>
 * Lanza excepciones:
 * <ul>
 *   <li>{@link ResourceNotFoundException} si la sucursal no existe</li>
 * </ul>
 * </p>
 *
 * @see ProductCreateRequest
 * @see ProductResponse
 * @see Product
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
