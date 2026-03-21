package com.accenture.franquicias_api.infrastructure.config;

import com.accenture.franquicias_api.infrastructure.security.JwtProvider;
import com.accenture.franquicias_api.infrastructure.security.JwtReactiveAuthenticationManager;
import com.accenture.franquicias_api.infrastructure.security.JwtServerAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración de seguridad JWT para Spring Security 6 WebFlux.
 *
 * <p>
 * <strong>PATRÓN CORRECTO EN WEBFLUX 6:</strong>
 * <ol>
 *   <li>Request llega con header Authorization: Bearer &lt;token&gt;</li>
 *   <li>AuthenticationWebFilter invoca al ServerAuthenticationConverter</li>
 *   <li>El converter extrae el token y crea Authentication sin validar</li>
 *   <li>El filter invoca al ReactiveAuthenticationManager</li>
 *   <li>El manager valida el token JWT y marca como autenticado</li>
 *   <li>Si es permitido → request procede al controlador</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>COMPONENTES:</strong>
 * <ul>
 *   <li>{@link JwtProvider} - Valida JWT y extrae claims</li>
 *   <li>{@link JwtServerAuthenticationConverter} - Extrae token del header</li>
 *   <li>{@link JwtReactiveAuthenticationManager} - Valida y marca Authentication</li>
 *   <li>{@link AuthenticationWebFilter} - Orquesta converter + manager</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>¿POR QUÉ NO USAR x?</strong>
 * <ul>
 *   <li>NoOpServerSecurityContextRepository - Deshabilita seg context completamente</li>
 *   <li>@Component WebFilter - No registra con Spring Security en WebFlux</li>
 *   <li>ServerSecurityContextRepository solo - No invoca converter sin manager</li>
 * </ul>
 * </p>
 *
 * @see JwtReactiveAuthenticationManager
 * @see JwtServerAuthenticationConverter
 * @see JwtProvider
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad con JWT Bearer token authentication.
     *
     * <p>
     * El flujo es:
     * <ol>
     *   <li>CORS permite preflight requests</li>
     *   <li>AuthenticationWebFilter + ServerAuthenticationConverter extraen JWT</li>
     *   <li>ReactiveAuthenticationManager valida y autentica</li>
     *   <li>Autorización verifica rutas públicas vs protegidas</li>
     * </ol>
     * </p>
     *
     * @param http ServerHttpSecurity para configurar seguridad
     * @param jwtProvider Proveedor JWT
     * @param authenticationManager Manager reactivo para validar tokens JWT
     * @return Cadena de filtros de seguridad
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            JwtProvider jwtProvider,
            ReactiveAuthenticationManager authenticationManager) {

        // Crear el converter que extrae tokens JWT del header Authorization
        JwtServerAuthenticationConverter authenticationConverter =
                new JwtServerAuthenticationConverter(jwtProvider);

        // Crear el filtro de autenticación que invoca converter + manager
        // Este es el componente MÁS IMPORTANTE en WebFlux 6
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter);

        // La configuración del SecurityContextRepository
        ServerSecurityContextRepository securityContextRepository =
                new WebSessionServerSecurityContextRepository();

        return http
                // ========================
                // CONFIGURACIÓN DE CORS
                // ========================
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ========================
                // CONFIGURACIÓN DE CSRF
                // ========================
                // Deshabilitar CSRF en APIs REST stateless (JWT no requiere sesiones ni CSRF)
                .csrf(csrf -> csrf.disable())

                // ========================
                // INYECTAR EL AUTHENTICATION WEB FILTER
                // ========================
                // Este es el paso crítico: inyectar nuestro filtro personalizado
                // que combina converter + manager
                // Debe ejecutarse ANTES que el filtro de autenticación por defecto
                .addFilterBefore(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)

                // ========================
                // CONFIGURACIÓN DEL SECURITY CONTEXT REPOSITORY
                // ========================
                .securityContextRepository(securityContextRepository)

                // ========================
                // DESHABILITAR OTROS ESQUEMAS
                // ========================
                // HTTP Basic: usamos JWT en lugar de usuario/contraseña
                .httpBasic(basic -> basic.disable())

                // Form Login: usamos JWT en lugar de formularios HTML
                .formLogin(form -> form.disable())

                // ========================
                // CONFIGURACIÓN DE AUTORIZACIÓN (RUTAS)
                // ========================
                .authorizeExchange(authorize -> authorize
                        // ⭐ RUTAS PÚBLICAS (sin autenticación requerida)
                        .pathMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        // Rutas de salud (para load balancers, health checks)
                        .pathMatchers("/actuator/**").permitAll()

                        // Rutas de Swagger UI y OpenAPI (documentación)
                        .pathMatchers("/swagger-ui.html").permitAll()
                        .pathMatchers("/swagger-ui/**").permitAll()
                        .pathMatchers("/v3/api-docs").permitAll()
                        .pathMatchers("/v3/api-docs/**").permitAll()
                        .pathMatchers("/webjars/**").permitAll()

                        // ⭐ RUTAS PROTEGIDAS (requieren JWT válido)
                        // El AuthenticationWebFilter validará automáticamente
                        .anyExchange().authenticated()
                )

                .build();
    }

    /**
     * Configuración de CORS para permitir solicitudes desde navegadores
     * y frontends (Swagger UI, SPAs, etc).
     *
     * @return CorsConfigurationSource configurado
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // ⚠️ Cambiar en producción a: config.setAllowedOrigins(List.of("https://tudominio.com"))
        config.setAllowedOriginPatterns(Arrays.asList("*"));

        // Permitir todos los métodos HTTP estándar
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Permitir todos los headers (incluyendo Authorization: Bearer <token>)
        config.setAllowedHeaders(Arrays.asList("*"));

        // No requerir credenciales (JWT es stateless)
        config.setAllowCredentials(false);

        // Cache de respuestas preflight por 1 hora
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    /**
     * Bean para codificar contraseñas usando BCrypt.
     * Requerido por LoginUseCase para validar contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
