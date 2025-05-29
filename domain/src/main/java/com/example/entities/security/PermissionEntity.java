package com.example.entities.security;

import com.example.entities.security.enums.Permission;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Permission name;

}
