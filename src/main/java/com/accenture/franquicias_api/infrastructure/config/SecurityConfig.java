package com.accenture.franquicias_api.infrastructure.config;

import com.accenture.franquicias_api.infrastructure.security.SecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

/**
 * Configuración de seguridad para la API con autenticación JWT.
 *
 * <p>
 * Define:
 * <ul>
 *   <li>Rutas públicas: /api/auth/register, /api/auth/login, /swagger-ui/**</li>
 *   <li>Rutas protegidas: Todas las demás requieren token JWT válido</li>
 *   <li>Filtro de seguridad: SecurityFilter para validar JWT</li>
 *   <li>CORS: Permitir todas las origins y métodos HTTP</li>
 *   <li>Sin sesiones: Stateless (CSRF deshabilitado)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Integra con {@link SecurityFilter} para procesar tokens JWT en cada request.
 * </p>
 *
 * @see SecurityFilter
 * @see JwtProvider
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    /**
     * Configura la cadena de filtros de seguridad.
     * Define qué rutas son públicas y cuáles requieren autenticación.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            // Deshabilitar CSRF (no es necesario en APIs REST sin sesiones)
            .csrf(csrf -> csrf.disable())
            
            // Configurar contexto de seguridad sin sesiones
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            
            // Autorización de autoridades
            .authorizeExchange(authorize -> authorize
                // Rutas públicas de autenticación
                .pathMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                
                // Rutas de salud y actuador
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Todas las demás rutas requieren autenticación
                .anyExchange().authenticated()
            )
            
            .build();
    }

    /**
     * Bean para codificar contraseñas usando BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
