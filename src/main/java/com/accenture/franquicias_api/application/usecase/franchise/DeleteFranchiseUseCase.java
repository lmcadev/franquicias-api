package com.accenture.franquicias_api.application.usecase.franchise;

import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Caso de uso para eliminar una franquicia.
 *
 * <p>
 * Realiza las siguientes operaciones:
 * <ul>
 *   <li>Busca la franquicia a eliminar</li>
 *   <li>Implementa eliminación suave (soft delete)</li>
 *   <li>Establece el timestamp deleted_at sin eliminar físicamente</li>
 *   <li>Retorna confirmación de eliminación</li>
 * </ul>
 * </p>
 *
 * <p>
 * Lanza excepciones:
 * <ul>
 *   <li>{@link ResourceNotFoundException} si la franquicia no existe</li>
 * </ul>
 * </p>
 */
@Component
@RequiredArgsConstructor
public class DeleteFranchiseUseCase {
    
    private final FranchiseRepository franchiseRepository;
    
    public Mono<Void> execute(Long franchiseId) {
        // Verificar que la franquicia existe
        return franchiseRepository.findById(franchiseId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia", String.valueOf(franchiseId))))
            .flatMap(franchise -> franchiseRepository.delete(franchiseId));
    }
}
