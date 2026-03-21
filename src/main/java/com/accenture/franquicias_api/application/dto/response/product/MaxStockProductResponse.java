package com.accenture.franquicias_api.application.dto.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuesta especializada del producto con máximo stock en una franquicia.
 *
 * <p>
 * Utilizado en el endpoint GET /api/franchises/{franchiseId}/products/max-stock
 * para retornar información enriquecida del producto con mayor stock incluyendo
 * detalles de franquicia y sucursal.
 * </p>
 *
 * <p>
 * Campos incluidos:
 * <ul>
 *   <li>franchise_id: ID de la franquicia</li>
 *   <li>franchise_name: Nombre de la franquicia</li>
 *   <li>branch_id: ID de la sucursal que contiene el producto</li>
 *   <li>branch_name: Nombre de la sucursal</li>
 *   <li>product_id: ID del producto con mayor stock</li>
 *   <li>product_name: Nombre del producto</li>
 *   <li>max_stock: Cantidad de stock máximo encontrada</li>
 * </ul>
 * </p>
 *
 * @see com.accenture.franquicias_api.application.usecase.product.GetMaxStockProductByFranchiseUseCase
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaxStockProductResponse {

    @JsonProperty("franchise_id")
    private Long franchiseId;

    @JsonProperty("franchise_name")
    private String franchiseName;

    @JsonProperty("branch_id")
    private Long branchId;

    @JsonProperty("branch_name")
    private String branchName;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("max_stock")
    private Integer maxStock;
}
