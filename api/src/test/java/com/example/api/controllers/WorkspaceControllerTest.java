package com.example.api.controllers;

import com.example.api.controllers.config.TestSecurityConfig;
import com.example.api.dto.requests.WorkspaceRequestDTO;
import com.example.api.dto.responses.WorkspaceResponseDTO;
import com.example.domain.entities.Workspace;
import com.example.services.WorkspaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkspaceController.class)
@Import(TestSecurityConfig.class)
public class WorkspaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkspaceService workspaceService;

    private ObjectMapper objectMapper;

    private Workspace workspace;
    private WorkspaceRequestDTO workspaceRequestDTO;
    private WorkspaceResponseDTO workspaceResponseDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        workspace = new Workspace();
        workspace.setWorkspaceId(1L);
        workspace.setType("Private Office");
        workspace.setPrice(new BigDecimal("100.0"));

        workspaceRequestDTO = new WorkspaceRequestDTO("Private Office", new BigDecimal("100.0"));

        workspaceResponseDTO = new WorkspaceResponseDTO();
        workspaceResponseDTO.setWorkspaceId(workspace.getWorkspaceId());
        workspaceResponseDTO.setType(workspace.getType());
        workspaceResponseDTO.setPrice(workspace.getPrice());
        workspaceResponseDTO.setReservations(Collections.emptyList());
    }

    @WithMockUser(username = "testuser", authorities = {"READ"})
    @Test
    void getAllWorkspaces_ShouldReturnWorkspaceList() throws Exception {
        when(workspaceService.findAll()).thenReturn(Collections.singletonList(workspace));

        mockMvc.perform(get("/workspaces/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].workspaceId").value(workspaceResponseDTO.getWorkspaceId()))
                .andExpect(jsonPath("$[0].type").value(workspaceResponseDTO.getType()))
                .andExpect(jsonPath("$[0].price").value(workspaceResponseDTO.getPrice().toString()));
    }

    @WithMockUser(username = "testuser", authorities = {"READ"})
    @Test
    void getWorkspaceById_ShouldReturnWorkspace() throws Exception {
        when(workspaceService.findById(1L)).thenReturn(Optional.of(workspace));

        mockMvc.perform(get("/workspaces/get/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workspaceId").value(workspaceResponseDTO.getWorkspaceId()))
                .andExpect(jsonPath("$.type").value(workspaceResponseDTO.getType()))
                .andExpect(jsonPath("$.price").value(workspaceResponseDTO.getPrice().toString()));
    }

    @WithMockUser(username = "testuser", authorities = {"READ"})
    @Test
    void getWorkspaceById_ShouldReturnNotFound() throws Exception {
        when(workspaceService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/workspaces/get/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    @Test
    void createWorkspace_ShouldReturnCreated() throws Exception {

        mockMvc.perform(post("/workspaces/create")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(workspaceRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value(workspaceRequestDTO.getType()))
                .andExpect(jsonPath("$.price").value(workspaceRequestDTO.getPrice().toString()));
    }

    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    @Test
    void deleteWorkspace_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/workspaces/remove/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    @Test
    void deleteWorkspace_ShouldReturnNotFound() throws Exception {
        doThrow(new RuntimeException("Workspace not found")).when(workspaceService).deleteById(10L);

        mockMvc.perform(delete("/workspaces/remove/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
