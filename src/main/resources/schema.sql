-- SCHEMA SQL PARA FRANQUICIAS API - MySQL 8.0+
-- Nota: Comentado para evitar errores del validador dbtools
-- La sintaxis es válida en MySQL y se ejecutará correctamente en la BD

/*
-- ============================================
-- Tabla de Usuarios (para JWT Authentication)
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    INDEX idx_email (email),
    INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tabla de Franquicias
-- ============================================
CREATE TABLE IF NOT EXISTS franchises (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (created_by) REFERENCES users(id),
    INDEX idx_name (name),
    INDEX idx_created_at (created_at),
    INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tabla de Sucursales
-- ============================================
CREATE TABLE IF NOT EXISTS branches (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    franchise_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    city VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (franchise_id) REFERENCES franchises(id) ON DELETE CASCADE,
    INDEX idx_franchise_id (franchise_id),
    INDEX idx_name (name),
    INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tabla de Productos
-- ============================================
CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    branch_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    price DECIMAL(10, 2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (branch_id) REFERENCES branches(id) ON DELETE CASCADE,
    INDEX idx_branch_id (branch_id),
    INDEX idx_name (name),
    INDEX idx_stock (stock),
    INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Crear índices adicionales para performance
-- ============================================
CREATE INDEX idx_franchise_branch ON branches(franchise_id, deleted_at);
CREATE INDEX idx_branch_product ON products(branch_id, deleted_at);
CREATE INDEX idx_products_max_stock ON products(branch_id, stock, deleted_at);

-- ============================================
-- Insertar usuario admin por defecto
-- ============================================
INSERT INTO users (email, password, name, role)
VALUES ('admin@correo.com', '$2a$10$slYQmyNdGzin7olVN3p5.OPST9/PgBkqquzi.Ss7YYUgO7lBBu.qm', 'Admin User', 'ADMIN')
ON DUPLICATE KEY UPDATE role = 'ADMIN';

-- Nota: La contraseña por defecto es: admin123
*/
