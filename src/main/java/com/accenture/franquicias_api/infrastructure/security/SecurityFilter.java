package com.accenture.franquicias_api.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Filtro web reactivo para autenticación JWT en Spring WebFlux.
 *
 * <p>
 * En cada request HTTP:
 * <ul>
 *   <li>Extrae el token JWT del header Authorization (Bearer <token>)</li>
 *   <li>Valida el token usando {@link JwtProvider}</li>
 *   <li>Extrae userId y email del token válido</li>
 *   <li>Configura el contexto de seguridad (SecurityContext)</li>
 *   <li>Permite que el request continúe en la cadena de filtros</li>
 * </ul>
 * </p>
 *
 * <p>
 * Si el token es inválido o no existe, el request continúa sin autenticación
 * (será rechazado por SecurityConfig en rutas protegidas).
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityFilter implements WebFilter {

    private final JwtProvider jwtProvider;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        try {
            String token = extractToken(exchange);

            if (token != null && jwtProvider.validateToken(token)) {
                Long userId = jwtProvider.getUserIdFromToken(token);
                String email = jwtProvider.getEmailFromToken(token);

                // Crear autenticación con userId y email
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(email, null, null);
                
                // Guardar userId en los atributos para acceso posterior
                authentication.setDetails(userId);

                log.debug("Token JWT validado para usuario: {}", email);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.debug("Error al procesar token JWT: {}", e.getMessage());
        }

        return chain.filter(exchange);
    }

    /**
     * Extrae el token JWT del header Authorization.
     * Espera formato: "Bearer <token>"
     *
     * @param exchange ServerWebExchange
     * @return Token sin el prefijo Bearer, o null si no existe
     */
    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest()
            .getHeaders()
            .getFirst(AUTHORIZATION_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
