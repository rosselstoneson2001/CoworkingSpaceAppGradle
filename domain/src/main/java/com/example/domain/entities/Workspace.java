package com.example.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_id")
    private Long workspaceId;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "workspace", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Reservation> reservations = new ArrayList<>();

    @Override
    public String toString() {
        return "\n================================" +
                "\nWorkspace ID: " + workspaceId +
                "\nType: " + type +
                "\nPrice: $" + price +
                "\n===============================";
    }
}
