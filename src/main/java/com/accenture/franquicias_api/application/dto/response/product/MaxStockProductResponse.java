package com.accenture.franquicias_api.application.dto.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuesta del producto con mayor stock por sucursal
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
