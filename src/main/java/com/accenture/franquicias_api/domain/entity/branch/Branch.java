package com.accenture.franquicias_api.domain.entity.branch;

import com.accenture.franquicias_api.domain.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad de Sucursal de la capa de dominio.
 * 
 * Representa una ubicación física de una franquicia.
 * Las sucursales están anidadas bajo una franquicia existente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Branch", description = "Entidad de sucursal - ubicación física de una franquicia")
public class Branch extends BaseEntity {

    @Schema(description = "ID de la franquicia a la que pertenece esta sucursal", example = "1")
    private Long franchiseId;

    @Schema(description = "Nombre de la sucursal", example = "Accenture Medellín")
    private String name;

    @Schema(description = "Dirección completa de la sucursal", example = "Calle 32 #45-67")
    private String address;

    @Schema(description = "Ciudad donde está ubicada la sucursal", example = "Medellín")
    private String city;
}
