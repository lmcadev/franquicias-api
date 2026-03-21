package com.accenture.franquicias_api.application.mapper.branch;

import com.accenture.franquicias_api.application.dto.request.branch.BranchCreateRequest;
import com.accenture.franquicias_api.application.dto.request.branch.BranchUpdateRequest;
import com.accenture.franquicias_api.application.dto.response.branch.BranchResponse;
import com.accenture.franquicias_api.domain.entity.branch.Branch;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper para conversión entre DTOs de Sucursal y entidad de dominio.
 *
 * <p>
 * Utiliza MapStruct para generar implementaciones en tiempo de compilación.
 * Realiza conversiones entre:
 * <ul>
 *   <li>Solicitudes de creación ({@link BranchCreateRequest}) → Entidad Sucursal</li>
 *   <li>Solicitudes de actualización ({@link BranchUpdateRequest}) → Entidad Sucursal</li>
 *   <li>Entidad Sucursal → Respuesta ({@link BranchResponse})</li>
 *   <li>Lista de entidades → Lista de respuestas</li>
 * </ul>
 * </p>
 *
 * @see Branch
 * @see BranchCreateRequest
 * @see BranchUpdateRequest
 * @see BranchResponse
 */
@Mapper(componentModel = "spring")
public interface BranchMapper {
    /**
     * Convierte una solicitud de creación a entidad Sucursal.
     *
     * @param request la solicitud de creación
     * @return la entidad Sucursal con valores de la solicitud
     */
    Branch toDomain(BranchCreateRequest request);

    /**
     * Convierte una solicitud de actualización a entidad Sucursal.
     *
     * @param request la solicitud de actualización
     * @return la entidad Sucursal con valores actualizados
     */
    Branch toDomain(BranchUpdateRequest request);

    /**
     * Convierte una entidad Sucursal a respuesta API.
     *
     * @param domain la entidad Sucursal
     * @return la respuesta con datos de la sucursal
     */
    BranchResponse toResponse(Branch domain);

    /**
     * Convierte una lista de entidades Sucursal a lista de respuestas API.
     *
     * @param domains lista de entidades Sucursal
     * @return lista de respuestas API
     */
    List<BranchResponse> toResponseList(List<Branch> domains);
}
