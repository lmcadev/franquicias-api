package com.accenture.franquicias_api.infrastructure.persistence.entity.branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("branches")
public class BranchEntity {
    @Id
    private Long id;

    @Column("franchise_id")
    private Long franchiseId;

    @Column("name")
    private String name;

    @Column("address")
    private String address;

    @Column("city")
    private String city;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("deleted_at")
    private LocalDateTime deletedAt;
}
