package com.accenture.franquicias_api.infrastructure.persistence.mapper.franchise;

import com.accenture.franquicias_api.domain.entity.franchise.Franchise;
import com.accenture.franquicias_api.infrastructure.persistence.entity.franchise.FranchiseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FranchiseEntityMapper {
    Franchise toDomain(FranchiseEntity entity);
    FranchiseEntity toEntity(Franchise domain);
}
