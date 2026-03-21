package com.accenture.franquicias_api.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Gestor reactivo de autenticación JWT para Spring Security 6 WebFlux.
 *
 * <p>
 * En Spring Security 6 WebFlux, el flujo correcto de autenticación JWT es:
 * <ol>
 *   <li>ServerAuthenticationConverter extrae el token del header y crea una Authentication sin validar</li>
 *   <li>ReactiveAuthenticationManager (esta clase) valida la Authentication y la marca como autenticada</li>
 *   <li>SecurityConfig -> ServerHttpSecurity configura qué permite pasar sin autenticación</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>IMPORTANTE:</strong> Los enfoques obsoletos en WebFlux son:
 * <ul>
 *   <li>@Component WebFilter: No funciona en WebFlux para auth (solo en servlet)</li>
 *   <li>ServerSecurityContextRepository: No se invoca pronto suficiente en WebFlux</li>
 *   <li>Esperar que el converter haga toda la autenticación: Spring lo ignora sin un AuthenticationManager</li>
 * </ul>
 * </p>
 *
 * <p>
 * El flujo correcto es:
 * <pre>
 *   Request → ServerAuthenticationConverter extrae token → crea UsernamePasswordAuthenticationToken
 *   ↓
 *   ReactiveAuthenticationManager.authenticate() valida JWT
 *   ↓
 *   Retorna Authentication autenticado (isAuthenticated() = true)
 *   ↓
 *   SecurityConfig pasa el request al controlador
 * </pre>
 * </p>
 *
 * @see JwtServerAuthenticationConverter
 * @see JwtProvider
 * @see org.springframework.security.config.web.server.ServerHttpSecurity
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtProvider jwtProvider;

    /**
     * Procesa una Authentication JWT (debe venir del ServerAuthenticationConverter).
     *
     * <p>
     * Expectativas de entrada:
     * <ul>
     *   <li>authentication.getName() → email del usuario</li>
     *   <li>authentication.getCredentials() → null (sin credenciales en JWT)</li>
     *   <li>authentication.isAuthenticated() → false (aún no validada)</li>
     * </ul>
     * </p>
     *
     * <p>
     * Lógica:
     * <ol>
     *   <li>Valida que la Authentication sea una UsernamePasswordAuthenticationToken</li>
     *   <li>Extrae el principal (email) y busca el token en la Authentication</li>
     *   <li>Valida el token JWT usando JwtProvider</li>
     *   <li>Si es válido, marca como autenticado y retorna</li>
     *   <li>Si es inválido, lanza BadCredentialsException</li>
     * </ol>
     * </p>
     *
     * @param authentication Authentication del ServerAuthenticationConverter
     * @return Mono con Authentication autenticado, o error si es inválido
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        // En WebFlux, los componentes deben ser reactivos
        // Ejecutar la lógica de validación en un scheduler separado para no bloquear
        return Mono.fromCallable(() -> {
            log.debug("JwtReactiveAuthenticationManager.authenticate() procesando: {}",
                    authentication.getName());

            // Solo procesar UsernamePasswordAuthenticationToken
            if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
                log.warn("Authentication no es UsernamePasswordAuthenticationToken: {}",
                        authentication.getClass().getSimpleName());
                throw new BadCredentialsException("Tipo de autenticación no soportado");
            }

            // Obtener el principal (email/username)
            String principal = authentication.getName();

            // En JwtServerAuthenticationConverter, los credentials se establecen al token
            // Pero usamos getCredentials() que puede ser null
            // Necesitamos obtener el token del contexto de la request
            String token = (String) authentication.getCredentials();

            if (token == null) {
                log.warn("No hay token en las credenciales para: {}", principal);
                throw new BadCredentialsException("Token JWT no proporcionado");
            }

            // Validar el token JWT
            if (!jwtProvider.validateToken(token)) {
                log.warn("Token JWT inválido para usuario: {}", principal);
                throw new BadCredentialsException("Token JWT inválido o expirado");
            }

            // Extraer información del token
            Long userId = jwtProvider.getUserIdFromToken(token);
            String email = jwtProvider.getEmailFromToken(token);

            log.info("JWT autenticado exitosamente: {} (ID: {})", email, userId);

            // Crear Authentication autenticado
            // IMPORTANTE: setAuthenticated(true) indica que ya fue validado
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            email,                          // principal
                            token,                          // credentials (mantener para acceso posterior)
                            AuthorityUtils.NO_AUTHORITIES   // authorities - NO NULL
                    );

            // Establecer detalles adicionales (userId)
            authToken.setDetails(userId);

            return (Authentication) authToken;
        })
        // Capturar excepciones y convertir a BadCredentialsException si es necesario
        .onErrorMap(this::mapException)
        // Usar default scheduler para no bloquear
        .subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic());
    }

    /**
     * Mapea excepciones a BadCredentialsException si corresponde.
     */
    private Throwable mapException(Throwable throwable) {
        if (throwable instanceof BadCredentialsException) {
            return throwable;
        }
        log.error("Error inesperado en autenticación JWT", throwable);
        return new BadCredentialsException("Error al procesar autenticación JWT", throwable);
    }
}
