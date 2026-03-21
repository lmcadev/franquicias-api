package com.accenture.franquicias_api.application.usecase.branch;

import com.accenture.franquicias_api.presentation.exception.ResourceNotFoundException;
import com.accenture.franquicias_api.domain.repository.branch.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Caso de uso para eliminar una sucursal.
 *
 * <p>
 * Realiza las siguientes operaciones:
 * <ul>
 *   <li>Busca la sucursal a eliminar</li>
 *   <li>Implementa eliminación suave (soft delete)</li>
 *   <li>Establece el timestamp deleted_at sin eliminar físicamente</li>
 *   <li>Retorna confirmación de eliminación</li>
 * </ul>
 * </p>
 *
 * <p>
 * Lanza excepciones:
 * <ul>
 *   <li>{@link ResourceNotFoundException} si la sucursal no existe</li>
 * </ul>
 * </p>
 */
@Component
@RequiredArgsConstructor
public class DeleteBranchUseCase {
    
    private final BranchRepository branchRepository;
    
    public Mono<Void> execute(Long branchId) {
        // Verificar que la sucursal existe
        return branchRepository.findById(branchId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sucursal", String.valueOf(branchId))))
            .flatMap(branch -> branchRepository.delete(branchId));
    }
}
