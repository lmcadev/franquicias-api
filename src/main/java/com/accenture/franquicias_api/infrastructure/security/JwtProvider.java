package com.accenture.franquicias_api.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Proveedor de tokens JWT para autenticación de usuarios.
 *
 * <p>
 * Responsable de:
 * <ul>
 *   <li>Generar tokens JWT firmados con HS256</li>
 *   <li>Validar tokens JWT en requests</li>
 *   <li>Extraer claims (userId, email) de tokens válidos</li>
 *   <li>Manejo de expiración (configurable en propiedades)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Configuración requerida en application.yml:
 * <pre>
 * jwt:
 *   secret: clave-secreta-muy-larga-minimo-256-bits
 *   expiration: 3600000  # 1 hora en ms
 * </pre>
 * </p>
 *
 * <p>
 * Utiliza JJWT 0.11+ para operaciones criptográficas con HMAC-SHA256.
 * </p>
 */
@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    /**
     * Genera un token JWT con los claims del usuario.
     *
     * @param userId ID del usuario
     * @param email Email del usuario
     * @return Token JWT firmado
     */
    public String generateToken(Long userId, String email) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            
            return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        } catch (Exception e) {
            log.error("Error al generar token JWT", e);
            throw new RuntimeException("Error al generar token JWT", e);
        }
    }

    /**
     * Valida un token JWT.
     *
     * @param token Token a validar
     * @return true si es válido, false en caso contrario
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Token JWT inválido: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extrae el email (subject) del token JWT.
     *
     * @param token Token JWT
     * @return Email del usuario
     */
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extrae el userId del token JWT.
     *
     * @param token Token JWT
     * @return ID del usuario
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("userId", Long.class);
    }

    /**
     * Extrae el email del claim en el token JWT.
     *
     * @param token Token JWT
     * @return Email del usuario
     */
    public String getEmailClaimFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("email", String.class);
    }

    /**
     * Obtiene todos los claims del token JWT.
     *
     * @param token Token JWT
     * @return Claims del token
     */
    private Claims getClaims(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (Exception e) {
            log.error("Error al extraer claims del token: {}", e.getMessage());
            throw new RuntimeException("Token JWT inválido o expirado", e);
        }
    }
}
