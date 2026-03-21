package com.accenture.franquicias_api.infrastructure.persistence.mapper.branch;

import com.accenture.franquicias_api.domain.entity.branch.Branch;
import com.accenture.franquicias_api.infrastructure.persistence.entity.branch.BranchEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para conversión entre entidad de persistencia R2DBC y entidad de dominio de Sucursal.
 *
 * <p>
 * Utiliza MapStruct para generar implementaciones en tiempo de compilación.
 * Realiza conversión bidireccional entre:
 * <ul>
 *   <li>Entidad de persistencia ({@link BranchEntity}) ↔ Entidad de dominio ({@link Branch})</li>
 * </ul>
 * </p>
 *
 * <p>
 * Esta capa de mapeo desacopla completamente la capa de persistencia (R2DBC)
 * de la lógica de negocio (dominio), permitiendo cambios en la estructura
 * de la base de datos sin afectar el dominio.
 * </p>
 *
 * @see Branch
 * @see BranchEntity
 */
@Mapper(componentModel = "spring")
public interface BranchEntityMapper {
    /**
     * Convierte una entidad de persistencia a entidad de dominio.
     *
     * @param entity la entidad R2DBC de persistencia
     * @return la entidad de dominio con los mismos datos
     */
    Branch toDomain(BranchEntity entity);

    /**
     * Convierte una entidad de dominio a entidad de persistencia.
     *
     * @param domain la entidad de dominio
     * @return la entidad R2DBC de persistencia con los mismos datos
     */
    BranchEntity toEntity(Branch domain);
}
