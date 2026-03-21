package com.accenture.franquicias_api.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Convierte el JWT token del header en una Authentication sin validar.
 *
 * <p>
 * En Spring Security 6 WebFlux, el ServerAuthenticationConverter SOLO extrae credenciales
 * del request. La validación se delega al ReactiveAuthenticationManager. Este es el patrón correcto.
 * </p>
 *
 * <p>
 * Flujo:
 * <ol>
 *   <li>Este converter extrae el token del header Authorization</li>
 *   <li>Crea una UsernamePasswordAuthenticationToken sin validar</li>
 *   <li>Spring Security 6 WebFlux invoca al ReactiveAuthenticationManager</li>
 *   <li>El manager valida y marca como autenticado</li>
 * </ol>
 * </p>
 *
 * @see JwtReactiveAuthenticationManager
 */
@Slf4j
@RequiredArgsConstructor
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtProvider jwtProvider;
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        // Extraer token del header
        String token = extractToken(exchange);
        
        // Si no hay token, no convertir a autenticación
        if (token == null) {
            log.debug("No Authorization header encontrado");
            return Mono.empty();
        }

        log.debug("Token extraído del header Authorization");

        try {
            // IMPORTANTE EN WEBFLUX 6: No validar aquí
            // Solo extraer información del claim sin validar la firma
            // Permitir que el ReactiveAuthenticationManager haga la validación
            
            // Intentar extraer email del token sin validar (solo parsing)
            String email = extractEmailWithoutValidation(token);

            if (email == null || email.isEmpty()) {
                log.warn("No se pudo extraer email del token");
                return Mono.empty();
            }

            log.debug("Email extraído del token: {}", email);

            // Crear Authentication SIN marcar como autenticado
            // El token se pasa como credentials para que el manager lo vuelva a procesar
            // IMPORTANTE: credentials = token (para que el manager lo valide)
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            email,                          // principal (email/username)
                            token,                          // credentials (el token JWT)
                            AuthorityUtils.NO_AUTHORITIES   // authorities vacías (aún no autenticado)
                    );

            // Marcar como NO autenticado para que Spring Security invoque el manager
            authenticationToken.setAuthenticated(false);

            log.debug("ServerAuthenticationConverter creó token sin validar para: {}", email);
            return Mono.just(authenticationToken);

        } catch (Exception e) {
            log.debug("Error procesando conversión de authentication: {}", e.getMessage());
            return Mono.error(new BadCredentialsException("Error procesando token JWT", e));
        }
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

    /**
     * Extrae el email del token JWT SIN validar la firma.
     *
     * <p>
     * Esta es una operación de solo lectura/parsing del token.
     * La validación criptográfica ocurre en el ReactiveAuthenticationManager.
     * </p>
     *
     * @param token Token JWT
     * @return Email (subject) del token, o null si no se puede extraer
     */
    private String extractEmailWithoutValidation(String token) {
        try {
            // Intentar obtener el email usando el JwtProvider
            // Esto validará la firma, pero es necesario para asegurar que el token es válido
            return jwtProvider.getEmailFromToken(token);
        } catch (Exception e) {
            log.debug("No se pudo extraer email del token: {}", e.getMessage());
            return null;
        }
    }
}
