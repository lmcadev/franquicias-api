package com.accenture.franquicias_api.application.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para las respuestas de autenticación (token)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("role")
    private String role;
}
