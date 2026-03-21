package com.accenture.franquicias_api.application.mapper.franchise;

import com.accenture.franquicias_api.application.dto.request.franchise.FranchiseCreateRequest;
import com.accenture.franquicias_api.application.dto.request.franchise.FranchiseUpdateRequest;
import com.accenture.franquicias_api.application.dto.response.franchise.FranchiseResponse;
import com.accenture.franquicias_api.domain.entity.franchise.Franchise;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper para conversión entre DTOs de Franquicia y entidad de dominio.
 *
 * <p>
 * Utiliza MapStruct para generar implementaciones en tiempo de compilación.
 * Realiza conversiones entre:
 * <ul>
 *   <li>Solicitudes de creación ({@link FranchiseCreateRequest}) → Entidad Franquicia</li>
 *   <li>Solicitudes de actualización ({@link FranchiseUpdateRequest}) → Entidad Franquicia</li>
 *   <li>Entidad Franquicia → Respuesta ({@link FranchiseResponse})</li>
 *   <li>Lista de entidades → Lista de respuestas</li>
 * </ul>
 * </p>
 *
 * @see Franchise
 * @see FranchiseCreateRequest
 * @see FranchiseUpdateRequest
 * @see FranchiseResponse
 */
@Mapper(componentModel = "spring")
public interface FranchiseMapper {
    /**
     * Convierte una solicitud de creación a entidad Franquicia.
     *
     * @param request la solicitud de creación
     * @return la entidad Franquicia con valores de la solicitud
     */
    Franchise toDomain(FranchiseCreateRequest request);

    /**
     * Convierte una solicitud de actualización a entidad Franquicia.
     *
     * @param request la solicitud de actualización
     * @return la entidad Franquicia con valores actualizados
     */
    Franchise toDomain(FranchiseUpdateRequest request);

    /**
     * Convierte una entidad Franquicia a respuesta API.
     *
     * @param domain la entidad Franquicia
     * @return la respuesta con datos de la franquicia
     */
    FranchiseResponse toResponse(Franchise domain);

    /**
     * Convierte una lista de entidades Franquicia a lista de respuestas API.
     *
     * @param domains lista de entidades Franquicia
     * @return lista de respuestas API
     */
    List<FranchiseResponse> toResponseList(List<Franchise> domains);
}
