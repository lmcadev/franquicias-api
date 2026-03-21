package com.accenture.franquicias_api.infrastructure.config;

import com.accenture.franquicias_api.domain.entity.franchise.Franchise;
import com.accenture.franquicias_api.domain.entity.branch.Branch;
import com.accenture.franquicias_api.domain.entity.product.Product;
import com.accenture.franquicias_api.domain.repository.franchise.FranchiseRepository;
import com.accenture.franquicias_api.domain.repository.branch.BranchRepository;
import com.accenture.franquicias_api.domain.repository.product.ProductRepository;
import com.accenture.franquicias_api.domain.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Inicializador de datos de prueba para la aplicación.
 * 
 * <p>
 * <strong>Propósito:</strong> Popula la base de datos automáticamente en el primer arranque con datos de prueba:
 * </p>
 * <ul>
 *   <li>1 Franquicia: "Hamburgesas ricas"</li>
 *   <li>5 Sucursales en ciudades colombianas (Bogotá, Medellín, Cali)</li>
 *   <li>9 Productos por sucursal (tipos de hamburguesas y bebidas)</li>
 *   <li>Total: 45 productos distribuidos</li>
 * </ul>
 * 
 * <p>
 * <strong>Ciclo de vida:</strong>
 * </p>
 * <ol>
 *   <li>Se ejecuta cuando Spring emite ApplicationReadyEvent (aplicación lista)</li>
 *   <li>Espera a que AdminInitializer cree el usuario administrador (retry automático)</li>
 *   <li>Verifica si la franquicia ya existe (idempotencia)</li>
 *   <li>Si no existe, la crea; si ya existe, reutiliza el ID</li>
 *   <li>Crea sucursales de forma idempotente (no duplica si ya existen)</li>
 *   <li>Crea productos de forma idempotente por sucursal</li>
 * </ol>
 * 
 * <p>
 * <strong>Manejo de condiciones de carrera:</strong>
 * </p>
 * <ul>
 *   <li>AtomicBoolean INITIALIZATION_STARTED previene múltiples ejecuciones en el mismo JVM</li>
 *   <li>waitForAdminUser() con retry (30 intentos, 1s cada uno) espera a AdminInitializer</li>
 *   <li>findCanonicalFranchiseByName() resuelve franquicia por nombre después de save() para evitar null ID</li>
 *   <li>getAllExistingXXX() previene duplicados en reintentos o reinicios</li>
 *   <li>onErrorResume() recupera de fallos transitivos en la creación</li>
 * </ul>
 * 
 * <p>
 * <strong>Notas técnicas (Reactor + R2DBC):</strong>
 * </p>
 * <ul>
 *   <li>Usa programación reactiva (Mono/Flux) para no bloquear el thread</li>
 *   <li>R2DBC no siempre retorna ID generado en save(); findCanonicalFranchiseByName() lo resuelve</li>
 *   <li>Los logs INFO facilitan debugging en Docker logs</li>
 *   <li>Timeout de 120s evita que el proceso se cuelgue indefinidamente</li>
 * </ul>
 * 
 * <p>
 * <strong>Dependencias inyectadas:</strong>
 * <ul>
 *   <li>FranchiseRepository - CRUD para franquicias</li>
 *   <li>BranchRepository - CRUD para sucursales</li>
 *   <li>ProductRepository - CRUD para productos</li>
 *   <li>UserRepository - para buscar el usuario admin creado</li>
 * </ul>
 * </p>
 * 
 * @see AdminInitializer - Crea el usuario admin antes de que este inicializador se ejecute
 * @see com.accenture.franquicias_api.domain.entity.franchise.Franchise
 * @see com.accenture.franquicias_api.domain.entity.branch.Branch
 * @see com.accenture.franquicias_api.domain.entity.product.Product
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {

    // Constantes de configuración
    private static final String ADMIN_EMAIL = "admin@correo.com";
    private static final String FRANCHISE_NAME = "Hamburgesas ricas";
    private static final int ADMIN_LOOKUP_MAX_ATTEMPTS = 30;  // 30 intentos
    private static final Duration ADMIN_LOOKUP_INTERVAL = Duration.ofSeconds(1);  // 1 segundo entre intentos
    private static final AtomicBoolean INITIALIZATION_STARTED = new AtomicBoolean(false);  // Guard thread-safe

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * Punto de entrada del inicializador: ejecutado cuando Spring emite ApplicationReadyEvent.
     * 
     * <p>
     * Orquestación del flujo:
     * </p>
     * <ol>
     *   <li>Verifica que no se haya iniciado ya (evita duplicados)</li>
     *   <li>Espera a admin con reintentos automáticos (30 intentos, 1s cada uno)</li>
     *   <li>Inicia la cadena de creación de franquicia/sucursales/productos</li>
     *   <li>Maneja errores y timeout (120 segundos máximo)</li>
     * </ol>
     * 
     * <p>
     * <strong>Reactor pipeline:</strong>
     * </p>
     * <pre>
     * waitForAdminUser()                    // -> Obtener usuario admin
     *   .flatMap(admin -> 
     *     initializeFranchiseData(admin.id) // -> Crear franquicia/sucursales/productos
     *   )
     *   .timeout(120s)                       // -> Abortar si toma más de 120 segundos
     *   .subscribe(...)                      // -> Ejecutar el observable
     * </pre>
     * 
     * @see #waitForAdminUser()
     * @see #initializeFranchiseData(Long)
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        // Guard: prevenir múltiples ejecuciones en el mismo JVM
        // compareAndSet(false, true) retorna true solo la primera vez
        if (!INITIALIZATION_STARTED.compareAndSet(false, true)) {
            log.info("DataInitializer ya fue lanzado en este arranque. Omitiendo ejecución duplicada.");
            return;
        }

        try {
            log.info("Iniciando carga de datos de prueba...");

            // Flujo reactivo: esperar admin → crear franquicia → crear sucursales → crear productos
            waitForAdminUser()
                .flatMap(adminUser -> {
                    log.info("Usuario admin encontrado (ID: {})", adminUser.getId());
                    return initializeFranchiseData(adminUser.getId());
                })
                .doOnError(e -> log.error("Error al inicializar datos de prueba", e))
                .timeout(Duration.ofSeconds(120))  // Max 120 segundos en total
                .subscribe(
                    v -> log.info("Datos de prueba inicializados"),
                    e -> log.error("Error en suscripción de inicialización", e),
                    () -> log.info("Proceso de inicialización completado")
                );
            
        } catch (Exception e) {
            log.error("Error fatal al inicializar datos", e);
        }
    }

    /**
     * Espera a que el usuario administrador sea creado por AdminInitializer con reintentos automáticos.
     * 
     * <p>
     * <strong>Razón de los reintentos:</strong>
     * AdminInitializer puede haber sido programado más tarde en el ciclo de eventos Spring.
     * Este método retry automático resuelve condiciones de carrera de arranque.
     * </p>
     * 
     * <p>
     * <strong>Lógica:</strong>
     * </p>
     * <ol>
     *   <li>Flux.interval(0, 1s) genera eventos cada 1 segundo (comenzando ahora)</li>
     *   <li>.take(30) limita a 30 eventos (máximo 30 segundos de espera)</li>
     *   <li>.concatMap() busca admin en cada intento</li>
     *   <li>.next() obtiene el primer resultado exitoso</li>
     *   <li>.switchIfEmpty() error si no encontrado en 30 intentos</li>
     * </ol>
     * 
     * @return Mono con el usuario admin cuando sea encontrado
     * @throws RuntimeException si no se encuentra después de 30 intentos
     */
    private Mono<com.accenture.franquicias_api.domain.entity.user.User> waitForAdminUser() {
        return Flux.interval(Duration.ZERO, ADMIN_LOOKUP_INTERVAL)
            .take(ADMIN_LOOKUP_MAX_ATTEMPTS)
            .concatMap(attempt -> userRepository.findByEmail(ADMIN_EMAIL))
            .next()
            .switchIfEmpty(Mono.error(new RuntimeException(
                "Usuario administrador no encontrado después de " + ADMIN_LOOKUP_MAX_ATTEMPTS +
                " intentos. Verifica AdminInitializer y la conexión a BD."
            )));
    }

    /**
     * Inicializa los datos de franquicia, sucursales y productos.
     * 
     * <p>
     * <strong>Lógica (idempotencia + manejo de condiciones de carrera):</strong>
     * </p>
     * <ol>
     *   <li>Busca franquicia existente por nombre</li>
     *   <li>Si EXISTE: log y reutiliza su ID</li>
     *   <li>Si NO EXISTE:
     *       <ul>
     *         <li>Crea nueva franquicia con .save()</li>
     *         <li>Busca de nuevo por nombre para obtener ID válido</li>
     *         <li>Esto resuelve el problema de R2DBC no retornando ID a veces</li>
     *         <li>Si todo falla, intenta recovery con onErrorResume()</li>
     *       </ul>
     *   </li>
     *   <li>Con franquicia resuelta, procede a crear sucursales y productos</li>
     * </ol>
     * 
     * <p>
     * <strong>¿Por qué findCanonicalFranchiseByName() después de save()?</strong>
     * R2DBC (spring-r2dbc) a veces no retorna el ID generado en la response de save().
     * Al buscar nuevamente por nombre y obtener el ID de la BD, garantizamos un ID válido
     * para las consultas posteriores (crear sucursales con franchise_id correcto).
     * </p>
     * 
     * @param adminUserId ID del usuario administrador que creó esta data
     * @return Mono vacío cuando se complete
     * @see #findCanonicalFranchiseByName()
     * @see #createBranchesAndProducts(Long)
     */
    private Mono<Void> initializeFranchiseData(Long adminUserId) {
        // Resolver franquicia canónica y luego poblar sucursales/productos
        return findCanonicalFranchiseByName()
            .doOnNext(existing -> log.info("Franquicia '{}' ya existe con ID: {}", FRANCHISE_NAME, existing.getId()))
            .switchIfEmpty(Mono.defer(() -> {
                log.info("Creando franquicia '{}'...", FRANCHISE_NAME);
                Franchise franchise = new Franchise();
                franchise.setName(FRANCHISE_NAME);
                franchise.setDescription("Franquicia de comida rápida especializada en hamburguesas artesanales");
                franchise.setCreatedBy(adminUserId);

                // Guardar la franquicia
                return franchiseRepository.save(franchise)
                    // Problema: R2DBC puede no retornar el ID generado en la response
                    // Solución: Buscar de nuevo por nombre para obtener el ID valido de la BD
                    .flatMap(saved -> findCanonicalFranchiseByName().defaultIfEmpty(saved))
                    // Recuperación por condición de carrera: si alguien crea la franquicia al mismo tiempo
                    .onErrorResume(e -> {
                        log.warn("No se pudo crear franquicia por posible condición de carrera. Recuperando por nombre...", e);
                        return findCanonicalFranchiseByName();
                    })
                    // Si todo falla, error
                    .switchIfEmpty(Mono.error(new RuntimeException("No fue posible resolver la franquicia creada")));
            }))
            // Con la franquicia resuelta, crear sucursales y productos
            .flatMap(resolvedFranchise -> {
                log.info("Franquicia recuperada con ID: {}", resolvedFranchise.getId());
                return createBranchesAndProducts(resolvedFranchise.getId());
            });
    }

    /**
     * Busca la franquicia por nombre de forma canónica (determinista).
     * 
     * <p>
     * <strong>Propósito:</strong> Resolver la "franquicia canónica" de forma segura.
     * Si hay duplicados por condiciones de carrera, siempre retorna el más antiguo (ID menor).
     * </p>
     * 
     * <p>
     * <strong>Implementación:</strong>
     * </p>
     * <ol>
     *   <li>Obtiene TODOS los franchises (PageRequest con límite alto)</li>
     *   <li>Filtra por nombre (case-insensitive) = "Hamburgesas ricas"</li>
     *   <li>Ordena por ID ascendente (más antiguo primero)</li>
     *   <li>Retorna el primer resultado</li>
     * </ol>
     * 
     * <p>
     * <strong>¿Por qué esto es necesario?</strong>
     * Durante reinicios o condiciones de carrera, dos procesos pueden intentar crear la franquicia
     * simultáneamente. El UNIQUE constraint en el schema.sql previene duplicados a nivel BD,
     * pero su creación puede fallar intermitentemente. Este método siempre retorna el primero
     * (ID menor), garantizando consistencia incluso si hay reintentos.
     * </p>
     * 
     * @return Mono con la franquicia encontrada, o vacío si no existe
     * @see #initializeFranchiseData(Long)
     */
    private Mono<Franchise> findCanonicalFranchiseByName() {
        return franchiseRepository.findAll(PageRequest.of(0, 1000))
            // Filtrar por nombre exacto (case-insensitive)
            .filter(franchise -> franchise.getName() != null && franchise.getName().equalsIgnoreCase(FRANCHISE_NAME))
            // Ordenar por ID ascendente (el ID más bajo = el más antiguo = el verdadero)
            .sort((a, b) -> {
                Long left = a.getId() == null ? Long.MAX_VALUE : a.getId();
                Long right = b.getId() == null ? Long.MAX_VALUE : b.getId();
                return left.compareTo(right);
            })
            // Obtener el primero (más antiguo/primero creado)
            .next();
    }
    /**
     * Crea sucursales (de forma idempotente) y luego productos para cada sucursal.
     * 
     * <p>
     * <strong>Lógica de idempotencia:</strong>
     * </p>
     * <ol>
     *   <li>Obtiene todas las sucursales existentes para este franchise</li>
     *   <li>Para cada sucursal en el dataset:
     *       <ul>
     *         <li>Si YA EXISTE (por nombre): log y salta a siguiente</li>
     *         <li>Si NO EXISTE: crea e inmediatamente agrega a lista existente</li>
     *       </ul>
     *   </li>
     *   <li>Para cada sucursal (nueva o existente): crea productos idempotentemente</li>
     * </ol>
     * 
     * <p>
     * <strong>Dataset de sucursales:</strong>
     * </p>
     * <ul>
     *   <li>Bogotá Centro - Calle 72 #10-45</li>
     *   <li>Bogotá norte - Carrera 7 #123-456</li>
     *   <li>Medellín Centro - Calle 50 #45-67</li>
     *   <li>Medellín Laureles - Calle 33 #76-89</li>
     *   <li>Cali Centro - Calle 5 #66-77</li>
     * </ul>
     * 
     * @param franchiseId ID de la franquicia a la que pertenecen estas sucursales
     * @return Mono vacío cuando se completen todas las sucursales y productos
     * @see #createProductsForBranch(Long)
     */
    private Mono<Void> createBranchesAndProducts(Long franchiseId) {
        // Datos de sucursales en Colombia
        var branchesData = new Object[][] {
            {"Bogotá Centro", "Calle 72 #10-45", "Bogotá"},
            {"Bogotá norte", "Carrera 7 #123-456", "Bogotá"},
            {"Medellín Centro", "Calle 50 #45-67", "Medellín"},
            {"Medellín Laureles", "Calle 33 #76-89", "Medellín"},
            {"Cali Centro", "Calle 5 #66-77", "Cali"}
        };

        return branchRepository.findByFranchiseId(franchiseId, PageRequest.of(0, 1000))
            .collectList()
            .flatMapMany(existingBranches -> Flux.fromArray(branchesData)
                .concatMap(data -> {
                    String name = (String) data[0];
                    String address = (String) data[1];
                    String city = (String) data[2];

                    Mono<Branch> branchMono = Flux.fromIterable(existingBranches)
                        .filter(existing -> existing.getName() != null && existing.getName().equalsIgnoreCase(name))
                        .next()
                        .doOnNext(existing -> log.info("Sucursal ya existe: {} (ID: {})", name, existing.getId()))
                        .switchIfEmpty(Mono.defer(() -> {
                            Branch branch = new Branch();
                            branch.setFranchiseId(franchiseId);
                            branch.setName(name);
                            branch.setAddress(address);
                            branch.setCity(city);

                            return branchRepository.save(branch)
                                .doOnSuccess(savedBranch -> {
                                    log.info("Sucursal creada: {} (ID: {})", name, savedBranch.getId());
                                    existingBranches.add(savedBranch);
                                });
                        }));

                    return branchMono
                        .flatMap(branch -> createProductsForBranch(branch.getId()))
                        .doOnError(e -> log.error("Error procesando sucursal: {}", name, e));
                })
            )
            .then();
    }

    /**
     * Crea productos (de forma idempotente) para una sucursal.
     * 
     * <p>
     * <strong>Lógica de idempotencia:</strong>
     * </p>
     * <ol>
     *   <li>Obtiene todos los productos existentes para esta sucursal</li>
     *   <li>Para cada producto en el dataset:
     *       <ul>
     *         <li>Si YA EXISTE (por nombre): log y salta a siguiente</li>
     *         <li>Si NO EXISTE: crea e inmediatamente agrega a lista existente</li>
     *       </ul>
     *   </li>
     * </ol>
     * 
     * <p>
     * <strong>Dataset de productos (9 items):</strong>
     * </p>
     * <ul>
     *   <li>Hamburguesa Simple (150g) - $15.000</li>
     *   <li>Hamburguesa Doble (2x150g) - $22.000</li>
     *   <li>Hamburguesa Picante (jalapeños) - $18.000</li>
     *   <li>Bacon Burger (bacon+cheddar) - $20.000</li>
     *   <li>Mushroom Burger (champiñones) - $19.000</li>
     *   <li>Arroz con hamburguesa - $14.000</li>
     *   <li>Papas a la francesa - $8.000</li>
     *   <li>Gaseosa Grande (400ml) - $3.500</li>
     *   <li>Cerveza Artesanal (330ml) - $6.000</li>
     * </ul>
     * 
     * <p>
     * <strong>Nota:</strong> Se crean 9 productos × 5 sucursales = 45 productos en total.
     * </p>
     * 
     * @param branchId ID de la sucursal para la que se crean los productos
     * @return Mono vacío cuando se completen todos los productos de esta sucursal
     */
    private Mono<Void> createProductsForBranch(Long branchId) {
        // Menú de hamburguesas
        var products = new Object[][] {
            {"Hamburguesa Simple", "Pan, carne 150g, lechuga, tomate, cebolla", 35, 15000},
            {"Hamburguesa Doble", "Pan, 2 carnes 150g c/u, queso, lechuga, tomate", 25, 22000},
            {"Hamburguesa Picante", "Pan, carne 150g, jalapeños, salsa BBQ, cebolla caramelizada", 30, 18000},
            {"Bacon Burger", "Pan, carne 150g, bacon crujiente, queso cheddar, mayonesa", 20, 20000},
            {"Mushroom Burger", "Pan, carne 150g, champiñones salteados, queso suizo", 18, 19000},
            {"Arroz con hamburguesa", "Arroz blanco, hamburguesa casera, huevo frito", 40, 14000},
            {"Papas a la francesa", "Corte tradicional, sal, salsa de queso", 60, 8000},
            {"Gaseosa Grande", "Coca Cola, Sprite, Fanta - 400ml", 80, 3500},
            {"Cerveza Artesanal", "Selección de cervezas locales - 330ml", 50, 6000}
        };

        return productRepository.findByBranchId(branchId, PageRequest.of(0, 2000))
            .collectList()
            .flatMapMany(existingProducts -> Flux.fromArray(products)
                .concatMap(data -> {
                    String name = (String) data[0];
                    String description = (String) data[1];
                    Integer stock = (Integer) data[2];
                    Integer price = (Integer) data[3];

                    boolean exists = existingProducts.stream()
                        .anyMatch(existing -> existing.getName() != null && existing.getName().equalsIgnoreCase(name));

                    if (exists) {
                        log.info("Producto ya existe en sucursal {}: {}", branchId, name);
                        return Mono.empty();
                    }

                    Product product = new Product();
                    product.setBranchId(branchId);
                    product.setName(name);
                    product.setDescription(description);
                    product.setStock(stock);
                    product.setPrice(BigDecimal.valueOf(price));

                    return productRepository.save(product)
                        .doOnSuccess(savedProduct -> {
                            log.info("Producto creado: {} (ID: {})", name, savedProduct.getId());
                            existingProducts.add(savedProduct);
                        })
                        .doOnError(e -> log.error("Error creando producto: {}", name, e));
                })
            )
            .then();
    }
}
