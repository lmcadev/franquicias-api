package com.accenture.franquicias_api.infrastructure.persistence.mapper.user;

import com.accenture.franquicias_api.domain.entity.user.User;
import com.accenture.franquicias_api.infrastructure.persistence.entity.user.UserEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para conversión entre entidad de persistencia R2DBC y entidad de dominio de Usuario.
 *
 * <p>
 * Utiliza MapStruct para generar implementaciones en tiempo de compilación.
 * Realiza conversión bidireccional entre:
 * <ul>
 *   <li>Entidad de persistencia ({@link UserEntity}) ↔ Entidad de dominio ({@link User})</li>
 * </ul>
 * </p>
 *
 * <p>
 * Esta capa de mapeo desacopla completamente la capa de persistencia (R2DBC)
 * de la lógica de negocio (dominio), permitiendo cambios en la estructura
 * de la base de datos sin afectar el dominio.
 * </p>
 *
 * @see User
 * @see UserEntity
 */
@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    /**
     * Convierte una entidad de persistencia a entidad de dominio.
     *
     * @param entity la entidad R2DBC de persistencia
     * @return la entidad de dominio con los mismos datos
     */
    User toDomain(UserEntity entity);

    /**
     * Convierte una entidad de dominio a entidad de persistencia.
     *
     * @param domain la entidad de dominio
     * @return la entidad R2DBC de persistencia con los mismos datos
     */
    UserEntity toEntity(User domain);
}
