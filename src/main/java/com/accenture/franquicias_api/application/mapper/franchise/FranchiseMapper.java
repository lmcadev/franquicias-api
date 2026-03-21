package com.accenture.franquicias_api.application.mapper.franchise;

import com.accenture.franquicias_api.application.dto.request.franchise.FranchiseCreateRequest;
import com.accenture.franquicias_api.application.dto.request.franchise.FranchiseUpdateRequest;
import com.accenture.franquicias_api.application.dto.response.franchise.FranchiseResponse;
import com.accenture.franquicias_api.domain.entity.franchise.Franchise;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FranchiseMapper {
    Franchise toDomain(FranchiseCreateRequest request);
    Franchise toDomain(FranchiseUpdateRequest request);
    FranchiseResponse toResponse(Franchise domain);
    List<FranchiseResponse> toResponseList(List<Franchise> domains);
}
