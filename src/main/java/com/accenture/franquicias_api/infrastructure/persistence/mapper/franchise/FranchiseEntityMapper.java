package com.accenture.franquicias_api.infrastructure.persistence.mapper.franchise;

import com.accenture.franquicias_api.domain.entity.franchise.Franchise;
import com.accenture.franquicias_api.infrastructure.persistence.entity.franchise.FranchiseEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para conversión entre entidad de persistencia R2DBC y entidad de dominio de Franquicia.
 *
 * <p>
 * Utiliza MapStruct para generar implementaciones en tiempo de compilación.
 * Realiza conversión bidireccional entre:
 * <ul>
 *   <li>Entidad de persistencia ({@link FranchiseEntity}) ↔ Entidad de dominio ({@link Franchise})</li>
 * </ul>
 * </p>
 *
 * <p>
 * Esta capa de mapeo desacopla completamente la capa de persistencia (R2DBC)
 * de la lógica de negocio (dominio), permitiendo cambios en la estructura
 * de la base de datos sin afectar el dominio.
 * </p>
 *
 * @see Franchise
 * @see FranchiseEntity
 */
@Mapper(componentModel = "spring")
public interface FranchiseEntityMapper {
    /**
     * Convierte una entidad de persistencia a entidad de dominio.
     *
     * @param entity la entidad R2DBC de persistencia
     * @return la entidad de dominio con los mismos datos
     */
    Franchise toDomain(FranchiseEntity entity);

    /**
     * Convierte una entidad de dominio a entidad de persistencia.
     *
     * @param domain la entidad de dominio
     * @return la entidad R2DBC de persistencia con los mismos datos
     */
    FranchiseEntity toEntity(Franchise domain);

    /**
     * Conversión manual de entidad de persistencia a dominio.
     * Asegura que todos los campos, incluyendo el ID generado, se mapeen correctamente.
     *
     * @param entity la entidad de persistencia
     * @return la entidad de dominio con ID explícitamente asignado
     */
    default Franchise toDomainManual(FranchiseEntity entity) {
        if (entity == null) {
            return null;
        }
        Franchise franchise = new Franchise();
        franchise.setId(entity.getId());
        franchise.setName(entity.getName());
        franchise.setDescription(entity.getDescription());
        franchise.setCreatedBy(entity.getCreatedBy());
        franchise.setCreatedAt(entity.getCreatedAt());
        franchise.setUpdatedAt(entity.getUpdatedAt());
        franchise.setDeletedAt(entity.getDeletedAt());
        return franchise;
    }
}
