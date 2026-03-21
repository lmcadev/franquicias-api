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

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toDomain(ProductCreateRequest request);
    Product toDomain(ProductStockUpdateRequest request);
    Product toDomain(ProductUpdateNameRequest request);
    ProductResponse toResponse(Product domain);
    List<ProductResponse> toResponseList(List<Product> domains);

    @Mapping(target = "franchiseId", source = "franchise.id")
    @Mapping(target = "franchiseName", source = "franchise.name")
    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName", source = "branch.name")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "maxStock", source = "product.stock")
    MaxStockProductResponse toMaxStockResponse(Product product, Branch branch, Franchise franchise);
}
