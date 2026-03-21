package com.accenture.franquicias_api.infrastructure.persistence.mapper.branch;

import com.accenture.franquicias_api.domain.entity.branch.Branch;
import com.accenture.franquicias_api.infrastructure.persistence.entity.branch.BranchEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BranchEntityMapper {
    Branch toDomain(BranchEntity entity);
    BranchEntity toEntity(Branch domain);
}
