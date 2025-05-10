package com.example.entities;

import java.math.BigDecimal;

public class Workspace {

    private Long workspaceId;
    private String type;
    private BigDecimal price;

    public Workspace(BigDecimal price,
                     String type) {
        this.price = price;
        this.type = type;
    }

    public Workspace() {} // For JSON Deserialization

    public Long getWorkspaceId() { return workspaceId; }
    public BigDecimal getPrice() { return price; }
    public String getType() { return type; }

    public void setWorkspaceId(Long workspaceId) { this.workspaceId = workspaceId; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return "\n================================" +
                "\nWorkspace ID: " + workspaceId +
                "\nType: " + type +
                "\nrice: $" + price +
                "\n===============================";
    }
}
