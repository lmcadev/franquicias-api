package com.accenture.franquicias_api.domain.entity.product;

import com.accenture.franquicias_api.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {
    private Long branchId;
    private String name;
    private String description;
    private Integer stock;
    private BigDecimal price;
}
