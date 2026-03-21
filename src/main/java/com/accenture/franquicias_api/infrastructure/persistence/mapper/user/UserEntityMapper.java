package com.accenture.franquicias_api.infrastructure.persistence.mapper.user;

import com.accenture.franquicias_api.domain.entity.user.User;
import com.accenture.franquicias_api.infrastructure.persistence.entity.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    User toDomain(UserEntity entity);
    UserEntity toEntity(User domain);
}
