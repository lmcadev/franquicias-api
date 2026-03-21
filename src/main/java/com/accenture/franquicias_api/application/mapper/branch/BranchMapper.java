package com.accenture.franquicias_api.application.mapper.branch;

import com.accenture.franquicias_api.application.dto.request.branch.BranchCreateRequest;
import com.accenture.franquicias_api.application.dto.request.branch.BranchUpdateRequest;
import com.accenture.franquicias_api.application.dto.response.branch.BranchResponse;
import com.accenture.franquicias_api.domain.entity.branch.Branch;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    Branch toDomain(BranchCreateRequest request);
    Branch toDomain(BranchUpdateRequest request);
    BranchResponse toResponse(Branch domain);
    List<BranchResponse> toResponseList(List<Branch> domains);
}
