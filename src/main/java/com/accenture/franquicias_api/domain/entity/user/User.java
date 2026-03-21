package com.accenture.franquicias_api.domain.entity.user;

import com.accenture.franquicias_api.domain.entity.BaseEntity;
import com.accenture.franquicias_api.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    private String email;
    private String password;
    private String name;
    private UserRole role;
    private Boolean active;
}
