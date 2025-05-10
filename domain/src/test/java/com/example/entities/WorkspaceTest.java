package com.example.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class WorkspaceTest {

    @Test
    void testWorkspaceCreation() {
        Workspace workspace = new Workspace(new BigDecimal("123.32"), "Test Workspace");

        assertEquals(new BigDecimal("123.32"), workspace.getPrice());
        assertNull(workspace.getWorkspaceId());
        assertEquals("Test Workspace", workspace.getType());

    }

    @Test
    void testWorkspaceSetters() {
        Workspace workspace = new Workspace();

        workspace.setWorkspaceId(10L);
        workspace.setPrice(new BigDecimal("200.50"));
        workspace.setType("Meeting Room");

        assertEquals(10L, workspace.getWorkspaceId());
        assertEquals(new BigDecimal("200.50"), workspace.getPrice());
        assertEquals("Meeting Room", workspace.getType());
    }


}
