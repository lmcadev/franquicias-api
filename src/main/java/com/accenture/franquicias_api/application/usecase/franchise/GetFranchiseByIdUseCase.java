package com.accenture.franquicias_api.application.usecase.franchise;

import com.accenture.franquicias_api.application.dto.response.franchise.FranchiseResponse;
import com.accenture.franquicias_api.application.mapper.franchise.FranchiseMapper;
import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Caso de uso para obtener una franquicia por su ID.
 *
 * <p>
 * Realiza las siguientes operaciones:
 * <ul>
 *   <li>Busca la franquicia en la base de datos</li>
 *   <li>Filtra registros eliminados suavemente</li>
 *   <li>Convierte la entidad a DTO de respuesta</li>
 * </ul>
 * </p>
 *
 * <p>
 * Lanza excepciones:
 * <ul>
 *   <li>{@link ResourceNotFoundException} si la franquicia no existe o está eliminada</li>
 * </ul>
 * </p>
 *
 * @see FranchiseResponse
 */
@Component
@RequiredArgsConstructor
public class GetFranchiseByIdUseCase {
    
    private final FranchiseRepository franchiseRepository;
    private final FranchiseMapper franchiseMapper;
    
    public Mono<FranchiseResponse> execute(Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia", String.valueOf(franchiseId))))
            .map(franchiseMapper::toResponse);
    }
}
