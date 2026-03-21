package com.accenture.franquicias_api.domain.entity.franchise;

import com.accenture.franquicias_api.domain.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad de Franquicia de la capa de dominio.
 * 
 * Representa una franquicia (ej: Accenture Bogotá, Accenture Medellín) que puede tener máltiples sucursales.
 * Las franquicias están asociadas a un usuario creador (propietario).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Franchise", description = "Entidad de franquicia - cabecera de negocio con sucursales")
public class Franchise extends BaseEntity {

    @Schema(description = "Nombre único de la franquicia", example = "Accenture Bogotá")
    private String name;

    @Schema(description = "Descripción detallada de la franquicia", example = "servicios de consultoría")
    private String description;

    @Schema(description = "ID del usuario que creó la franquicia (propietario)", example = "1")
    private Long createdBy;
}
