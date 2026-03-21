package com.accenture.franquicias_api.application.mapper.user;

import com.accenture.franquicias_api.application.dto.request.auth.AuthLoginRequest;
import com.accenture.franquicias_api.application.dto.request.auth.AuthRegisterRequest;
import com.accenture.franquicias_api.application.dto.response.auth.AuthTokenResponse;
import com.accenture.franquicias_api.domain.entity.user.User;
import com.accenture.franquicias_api.domain.enums.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "active", constant = "true")
    User toDomain(AuthRegisterRequest request);

    User toDomainFromLogin(AuthLoginRequest request);

    @Mapping(target = "expiresIn", expression = "java(86400000L)")
    @Mapping(target = "tokenType", constant = "Bearer")
    AuthTokenResponse toResponse(User user, String token);
}
