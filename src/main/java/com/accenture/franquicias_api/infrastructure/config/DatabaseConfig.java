package com.accenture.franquicias_api.infrastructure.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

/**
 * Configuración de la base de datos con R2DBC (Reactive Relational Database Connectivity).
 *
 * <p>
 * Configura:
 * <ul>
 *   <li>R2dbcEntityOperations: Template para operaciones reactivas</li>
 *   <li>DatabaseClient: Cliente para queries SQL reactivas</li>
 *   <li>TransactionalOperator: Manejo de transacciones reactivas</li>
 *   <li>ConnectionFactoryInitializer: Inicialización del esquema desde schema.sql</li>
 *   <li>Auditoría automática: Gestión de createdAt/updatedAt timestamps</li>
 * </ul>
 * </p>
 *
 * <p>
 * Permite operaciones no-bloqueantes con MySQL a través de driver R2DBC.
 * </p>
 *
 * @see org.springframework.data.r2dbc.repository.R2dbcRepository
 */
@Configuration
@EnableR2dbcRepositories(basePackages = "com.accenture.franquicias_api.infrastructure.persistence.r2dbc")
@EnableR2dbcAuditing
public class DatabaseConfig {

    @Bean
    public R2dbcEntityOperations r2dbcEntityOperations(ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }

    @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory);
    }

    @Bean
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));

        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}
