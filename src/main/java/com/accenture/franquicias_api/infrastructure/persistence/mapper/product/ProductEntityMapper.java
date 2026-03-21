package com.accenture.franquicias_api.infrastructure.persistence.mapper.product;

import com.accenture.franquicias_api.domain.entity.product.Product;
import com.accenture.franquicias_api.infrastructure.persistence.entity.product.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {
    Product toDomain(ProductEntity entity);
    ProductEntity toEntity(Product domain);
}
