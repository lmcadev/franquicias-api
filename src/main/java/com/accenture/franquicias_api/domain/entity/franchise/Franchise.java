package com.accenture.franquicias_api.domain.entity.franchise;

import com.accenture.franquicias_api.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Franchise extends BaseEntity {
    private String name;
    private String description;
    private Long createdBy;
}
