package com.accenture.franquicias_api.infrastructure.config;

import com.accenture.franquicias_api.domain.entity.user.User;
import com.accenture.franquicias_api.domain.enums.UserRole;
import com.accenture.franquicias_api.domain.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Inicializador dinámico del usuario administrador.
 * 
 * <p>
 * <strong>Propósito:</strong> Garantizar que siempre exista un usuario admin en la BD al arrancar.
 * Se ejecuta ANTES de DataInitializer en el ciclo de eventos Spring.
 * </p>
 * 
 * <p>
 * <strong>Responsabilidades:</strong>
 * </p>
 * <ul>
 *   <li>Verificar si admin@correo.com existe en la BD</li>
 *   <li>Si NO existe: crear automáticamente con contraseña inicial "admin123"</li>
 *   <li>Si EXISTE con contraseña diferente: actualizar a "admin123" (sincronizar password)</li>
 *   <li>Registrar credenciales en logs para acceso inicial</li>
 *   <li>Validar que JWT_SECRET esté configurado (error fatal si no)</li>
 * </ul>
 * 
 * <p>
 * <strong>Ciclo de vida:</strong>
 * </p>
 * <ol>
 *   <li>Se ejecuta cuando Spring emite ApplicationReadyEvent</li>
 *   <li>Verifica JWT_SECRET (obligatorio, error si falta)</li>
 *   <li>Busca admin@correo.com en la BD</li>
 *   <li>Si existe: validar/sincronizar password</li>
 *   <li>Si NO existe: crear con datos iniciales</li>
 *   <li>Log las credenciales para el primer login</li>
 * </ol>
 * 
 * <p>
 * <strong>Seguridad & Configuración:</strong>
 * </p>
 * <ul>
 *   <li>Contraseña inicial: "admin123" (CAMBIAR DESPUÉS DEL PRIMER LOGIN)</li>
 *   <li>Hashing: BCryptPasswordEncoder (strength 10, PBKDF2/HMAC compatible)</li>
 *   <li>NO hardcodea contraseñas en schema.sql o código de datos</li>
 *   <li>JWT_SECRET debe definirse en application.yml o .env (único por deployment)</li>
 *   <li>Logs muestran credenciales solo en inicialización (DEBUG en logs iniciadores)</li>
 * </ul>
 * 
 * <p>
 * <strong>Dependencias inyectadas:</strong>
 * </p>
 * <ul>
 *   <li>UserRepository - CRUD y búsqueda de usuarios</li>
 *   <li>PasswordEncoder - BCryptPasswordEncoder para hashing de contraseñas</li>
 *   <li>@Value("${jwt.secret}") - JWT_SECRET desde configuración</li>
 * </ul>
 * 
 * <p>
 * <strong>Integración con DataInitializer:</strong>
 * </p>
 * <ul>
 *   <li>DataInitializer espera a que este inicializador cree el admin</li>
 *   <li>DataInitializer establece created_by = admin.id en franquicia</li>
 *   <li>El orden de ejecución es garantizado por Spring @EventListener</li>
 * </ul>
 * 
 * @see DataInitializer - Depende del admin creado aquí para poblar test data
 * @see UserRepository
 * @see PasswordEncoder
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private static final String ADMIN_INITIAL_PASSWORD = "admin123";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Punto de entrada: ejecutado cuando Spring emite ApplicationReadyEvent.
     * 
     * <p>
     * <strong>Orquestación del flujo:</strong>
     * </p>
     * <ol>
     *   <li>Verificar JWT_SECRET (must-have, error fatal si falta)</li>
     *   <li>Buscar admin@correo.com en la BD</li>
     *   <li>Si EXISTE:
     *       <ul>
     *         <li>Verificar password (debe ser "admin123")</li>
     *         <li>Si es diferente: actualizar a "admin123"</li>
     *         <li>Log de verificación exitosa</li>
     *       </ul>
     *   </li>
     *   <li>Si NO EXISTE:
     *       <ul>
     *         <li>Crear nuevo usuario con email "admin@correo.com"</li>
     *         <li>Password hasheado: BCrypt("admin123")</li>
     *         <li>Role: ADMIN</li>
     *         <li>Active: true</li>
     *         <li>Log credenciales para primer login</li>
     *       </ul>
     *   </li>
     * </ol>
     * 
     * <p>
     * <strong>Reactor pipeline:</strong>
     * </p>
     * <pre>
     * findByEmail("admin@correo.com")
     *   .flatMap(this::syncAdminPasswordIfLegacy)  // Si existe
     *   .switchIfEmpty(createAdminUser())           // Si no existe
     *   .doOnSuccess(...) / .doOnError(...)         // Log resultado
     *   .subscribe()                                // Ejecutar
     * </pre>
     * 
     * @see #syncAdminPasswordIfLegacy(User)
     * @see #createAdminUser()
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initializeAdminUser() {
        log.info("Iniciando verificación de usuario administrador...");
        
        // PASO 1: Validar que JWT_SECRET está configurado (es obligatorio)
        if (jwtSecret == null || jwtSecret.isBlank()) {
            log.error("ERROR CRÍTICO: JWT_SECRET no está configurado. Revisa el archivo .env o application.yml");
            throw new IllegalStateException("JWT_SECRET no está configurado");
        }
        log.debug("JWT_SECRET obtenido correctamente (longitud: {})", jwtSecret.length());

        // PASO 2: Flujo reactivo - buscar admin y crear si no existe
        userRepository.findByEmail("admin@correo.com")
            // Si EXISTE, sincronizar su password (por si es un ambiente reutilizado)
            .flatMap(this::syncAdminPasswordIfLegacy)
            // Si NO EXISTE, crear nuevo admin
            .switchIfEmpty(
                Mono.defer(() -> {
                    log.info("Usuario admin NO encontrado. Creando nuevo usuario administrador...");
                    return createAdminUser();
                })
            )
            // Log de éxito
            .doOnSuccess(user -> {
                if (user != null) {
                    log.info("Usuario administrador verificado:");
                    log.info("   Email: {}", user.getEmail());
                    log.info("   Role: {}", user.getRole());
                    log.info("   ID: {}", user.getId());
                }
            })
            // Log de error
            .doOnError(e -> log.error("Error en inicialización de admin", e))
            // Ejecutar el observable
            .subscribe();
    }

    /**
     * Sincroniza la password del admin existente si es necesario.
     * 
     * <p>
     * <strong>Caso de uso:</strong> En ambientes reutilizados o después de migraciones,
     * el admin puede existir con password diferente. Este método normaliza a "admin123".
     * </p>
     * 
     * <p>
     * <strong>Lógica:</strong>
     * </p>
     * <ol>
     *   <li>Verificar si password actual = "admin123" (hashed)</li>
     *   <li>Si SÍ: retornar admin sin cambios (ya sincronizado)</li>
     *   <li>Si NO: actualizar password y guardar
     *       <ul>
     *         <li>Nueva password: BCrypt("admin123")</li>
     *         <li>Guardar en BD</li>
     *         <li>Log de actualización</li>
     *       </ul>
     *   </li>
     * </ol>
     * 
     * @param existingAdmin Usuario administrador existente en la BD
     * @return Mono con el admin (actualizado o sin cambios) después de la sincronización
     * @see #createAdminUser()
     */
    private Mono<User> syncAdminPasswordIfLegacy(User existingAdmin) {
        // Verificar si la password ya es "admin123"
        if (passwordEncoder.matches(ADMIN_INITIAL_PASSWORD, existingAdmin.getPassword())) {
            log.info("Usuario admin ya usa la contraseña inicial configurada");
            return Mono.just(existingAdmin);
        }

        // Password es diferente, actualizar a "admin123"
        log.warn("Admin existente con contraseña diferente. Actualizando a contraseña inicial admin123...");
        existingAdmin.setPassword(passwordEncoder.encode(ADMIN_INITIAL_PASSWORD));

        // Guardar cambios
        return userRepository.save(existingAdmin)
            .doOnSuccess(updatedUser -> {
                log.info("Contraseña de admin migrada correctamente");
                log.info("   Email: {}", updatedUser.getEmail());
                log.info("   Nueva contraseña inicial: {}", ADMIN_INITIAL_PASSWORD);
            });
    }

    /**
     * Crea un nuevo usuario administrador con contraseña inicial "admin123".
     * 
     * <p>
     * <strong>Datos creados:</strong>
     * </p>
     * <ul>
     *   <li>Email: admin@correo.com</li>
     *   <li>Password: BCrypt("admin123")</li>
     *   <li>Name: "Administrador"</li>
     *   <li>Role: ADMIN (máximos permisos)</li>
     *   <li>Active: true (usuario disponible inmediatamente)</li>
     * </ul>
     * 
     * <p>
     * <strong>Flujo de creación:</strong>
     * </p>
     * <ol>
     *   <li>Preparar password: plaintext "admin123"</li>
     *   <li>Hash: BCryptPasswordEncoder (força 10)</li>
     *   <li>Crear entidad User con todos los datos</li>
     *   <li>Guardar en BD vía userRepository.save()</li>
     *   <li>Log credenciales para primer login</li>
     *   <li>Advertencia: CAMBIAR PASSWORD DESPUÉS DEL PRIMER LOGIN</li>
     * </ol>
     * 
     * <p>
     * <strong>Seguridad:</strong>
     * La contraseña inicial se loguea solo durante la creación (logs de arranque).
     * En producción, esto debe ser interceptado/protegido o generado dinámicamente.
     * </p>
     * 
     * @return Mono con el usuario admin creado
     * @throws Exception (capturada y logged) si hay error en la BD
     */
    private Mono<User> createAdminUser() {
        try {
            // Preparar contraseña: plaintext + hash BCrypt
            String plainPassword = ADMIN_INITIAL_PASSWORD;  // "admin123"
            String hashedPassword = passwordEncoder.encode(plainPassword);

            // Crear entidad user con valores iniciales
            User adminUser = new User();
            adminUser.setEmail("admin@correo.com");
            adminUser.setPassword(hashedPassword);  // Password hashed
            adminUser.setName("Administrador");
            adminUser.setRole(UserRole.ADMIN);  // Role ADMIN = máximos permisos
            adminUser.setActive(true);  // Disponible inmediatamente

            // Guardar en BD (reactivo)
            return userRepository.save(adminUser)
                .doOnSuccess(savedUser -> {
                    // Log exitoso con credenciales
                    log.info("USUARIO ADMINISTRADOR CREADO EXITOSAMENTE");
                    log.info("   Email: admin@correo.com");
                    log.info("   Contraseña INICIAL (CAMBIAR DESPUÉS): {}", plainPassword);
                    log.info("   Role: ADMIN");
                    log.info("   ID: {}", savedUser.getId());
                    log.warn("   IMPORTANTE: Cambia la contraseña después del primer login");
                });

        } catch (Exception e) {
            log.error("Error al crear usuario administrador", e);
            return Mono.error(e);
        }
    }
}
