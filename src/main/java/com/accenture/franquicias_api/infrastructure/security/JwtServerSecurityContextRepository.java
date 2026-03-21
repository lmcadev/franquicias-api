package com.accenture.franquicias_api.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Implementación personalizada de ServerSecurityContextRepository para JWT.
 * 
 * Extrae el token JWT del header Authorization y lo valida directamente
 * en lugar de usar sesiones.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtServerSecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtProvider jwtProvider;
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String token = extractToken(exchange);
        
        log.debug("Loading security context, token present: {}", token != null);
        
        if (token != null && jwtProvider.validateToken(token)) {
            try {
                Long userId = jwtProvider.getUserIdFromToken(token);
                String email = jwtProvider.getEmailFromToken(token);
                
                log.debug("Token JWT validado para usuario: {} (ID: {})", email, userId);
                
                // Crear autenticación con userId y email
                // IMPORTANTE: proporcionar una lista de autoridades (aunque estén vacías)
                // Si noprovees autoridades, el token se considera no autenticado
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        email,  // principal
                        null,   // credentials
                        AuthorityUtils.NO_AUTHORITIES  // authorities - IMPORTANTE: no null
                    );
                authentication.setDetails(userId);
                
                log.debug("Authentication creado con éxito para usuario: {}", email);
                
                // Crear contexto de seguridad
                SecurityContext securityContext = new SecurityContextImpl(authentication);
                return Mono.just(securityContext);
            } catch (Exception e) {
                log.debug("Error al procesar token JWT: {}", e.getMessage());
                return Mono.empty();
            }
        }
        
        log.debug("Token JWT inválido o no presente");
        return Mono.empty();
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        // No-op: JWT es stateless, no necesita guardar contexto
        return Mono.empty();
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
