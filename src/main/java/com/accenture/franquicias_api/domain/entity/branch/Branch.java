package com.accenture.franquicias_api.domain.entity.branch;

import com.accenture.franquicias_api.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Branch extends BaseEntity {
    private Long franchiseId;
    private String name;
    private String address;
    private String city;
}
