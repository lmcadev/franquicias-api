package com.accenture.franquicias_api.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Clase base para todas las entidades del dominio
 * Proporciona campos comunes: id, timestamps, soft delete
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    protected Long id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected LocalDateTime deletedAt;

    /**
     * Verifica si la entidad está eliminada (soft delete)
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * Marca la entidad como eliminada
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
