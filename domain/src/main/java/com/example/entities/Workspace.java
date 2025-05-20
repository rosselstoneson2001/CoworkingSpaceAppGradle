package com.example.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
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


    public Workspace(BigDecimal price,
                     String type) {
        this.price = price;
        this.type = type;
    }

    public Workspace(Long workspaceId,
                     BigDecimal price,
                     String type) {
        this.workspaceId = workspaceId;
        this.price = price;
        this.type = type;
    }

    public Workspace() {} // For JSON Deserialization

    public Long getWorkspaceId() { return workspaceId; }
    public BigDecimal getPrice() { return price; }
    public String getType() { return type; }
    public List<Reservation> getReservations() { return reservations; }
    public boolean isActive() { return isActive; }

    public void setWorkspaceId(Long workspaceId) { this.workspaceId = workspaceId; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setType(String type) { this.type = type; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
    public void setActive(boolean active) { this.isActive = active; }


    @Override
    public String toString() {
        return "\n================================" +
                "\nWorkspace ID: " + workspaceId +
                "\nType: " + type +
                "\nPrice: $" + price +
                "\n===============================";
    }
}
