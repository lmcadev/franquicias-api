package com.accenture.franquicias_api.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
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
 *   <li>Configura el contexto de seguridad (SecurityContext) de forma reactiva</li>
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
@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtProvider jwtProvider;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("JwtAuthenticationFilter.filter() CALLED for {}", exchange.getRequest().getPath());
        String token = extractToken(exchange);
        log.debug("Token extracted: {}", token != null ? "YES" : "NO");

        if (token != null && jwtProvider.validateToken(token)) {
            try {
                Long userId = jwtProvider.getUserIdFromToken(token);
                String email = jwtProvider.getEmailFromToken(token);

                log.debug("Token JWT validado para usuario: {}", email);

                // Crear autenticación con userId y email
                // IMPORTANTE: proporcionar una lista de autoridades (aunque estén vacías)
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        email,  // principal
                        null,   // credentials
                        AuthorityUtils.NO_AUTHORITIES  // authorities - IMPORTANTE: no null
                    );
                authentication.setDetails(userId);

                // Crear contexto de seguridad
                SecurityContext securityContext = new SecurityContextImpl(authentication);
                
                log.debug("Contexto de seguridad creado para usuario: {}", email);
                
                // Propagar el contexto de seguridad reactivamente
                return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
            } catch (Exception e) {
                log.debug("Error al procesar token JWT: {}", e.getMessage());
                return chain.filter(exchange);
            }
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
