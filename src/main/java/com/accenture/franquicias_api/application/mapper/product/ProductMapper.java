package com.accenture.franquicias_api.application.mapper.product;

import com.accenture.franquicias_api.application.dto.request.product.ProductCreateRequest;
import com.accenture.franquicias_api.application.dto.request.product.ProductStockUpdateRequest;
import com.accenture.franquicias_api.application.dto.request.product.ProductUpdateNameRequest;
import com.accenture.franquicias_api.application.dto.response.product.ProductResponse;
import com.accenture.franquicias_api.application.dto.response.product.MaxStockProductResponse;
import com.accenture.franquicias_api.domain.entity.branch.Branch;
import com.accenture.franquicias_api.domain.entity.franchise.Franchise;
import com.accenture.franquicias_api.domain.entity.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper para conversión entre DTOs de Producto y entidad de dominio.
 *
 * <p>
 * Utiliza MapStruct para generar implementaciones en tiempo de compilación.
 * Realiza conversiones entre:
 * <ul>
 *   <li>Solicitudes de creación ({@link ProductCreateRequest}) → Entidad Producto</li>
 *   <li>Solicitudes de actualización de stock → Entidad Producto</li>
 *   <li>Solicitudes de actualización de nombre → Entidad Producto</li>
 *   <li>Entidad Producto → Respuesta ({@link ProductResponse})</li>
 *   <li>Producto + Sucursal + Franquicia → Respuesta de máximo stock</li>
 *   <li>Lista de entidades → Lista de respuestas</li>
 * </ul>
 * </p>
 *
 * @see Product
 * @see ProductCreateRequest
 * @see ProductStockUpdateRequest
 * @see ProductUpdateNameRequest
 * @see ProductResponse
 * @see MaxStockProductResponse
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {
    /**
     * Convierte una solicitud de creación a entidad Producto.
     *
     * @param request la solicitud de creación
     * @return la entidad Producto con valores de la solicitud
     */
    Product toDomain(ProductCreateRequest request);

    /**
     * Convierte una solicitud de actualización de stock a entidad Producto.
     *
     * @param request la solicitud de actualización de stock
     * @return la entidad Producto con stock actualizado
     */
    Product toDomain(ProductStockUpdateRequest request);

    /**
     * Convierte una solicitud de actualización de nombre a entidad Producto.
     *
     * @param request la solicitud de actualización de nombre
     * @return la entidad Producto con nombre actualizado
     */
    Product toDomain(ProductUpdateNameRequest request);

    /**
     * Convierte una entidad Producto a respuesta API.
     *
     * @param domain la entidad Producto
     * @return la respuesta con datos del producto
     */
    ProductResponse toResponse(Product domain);

    /**
     * Convierte una lista de entidades Producto a lista de respuestas API.
     *
     * @param domains lista de entidades Producto
     * @return lista de respuestas API
     */
    List<ProductResponse> toResponseList(List<Product> domains);

    /**
     * Convierte un producto con su sucursal y franquicia asociada a respuesta de máximo stock.
     * Realiza mapeos complejos de IDs y nombres desde las entidades relacionadas.
     *
     * @param product la entidad Producto
     * @param branch la sucursal donde se ubica el producto
     * @param franchise la franquicia propietaria
     * @return la respuesta con información enriquecida de máximo stock
     */
    @Mapping(target = "franchiseId", source = "franchise.id")
    @Mapping(target = "franchiseName", source = "franchise.name")
    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName", source = "branch.name")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "maxStock", source = "product.stock")
    MaxStockProductResponse toMaxStockResponse(Product product, Branch branch, Franchise franchise);
}
