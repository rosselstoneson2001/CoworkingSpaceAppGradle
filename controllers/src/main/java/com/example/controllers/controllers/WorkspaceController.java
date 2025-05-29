package com.example.controllers.controllers;

import com.example.converters.WorkspaceConverter;
import com.example.dto.requests.WorkspaceRequestDTO;
import com.example.dto.responses.WorkspaceResponseDTO;
import com.example.entities.Workspace;
import com.example.services.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing workspaces.
 * Provides endpoints to retrieve, create, and delete workspaces.
 *
 * <p><b>Endpoints:</b></p>
 * <ul>
 *     <li>GET /workspaces/get-all - Get all workspaces</li>
 *     <li>GET /workspaces/get/{id} - Get a workspace by ID</li>
 *     <li>POST /workspaces/create - Create a new workspace</li>
 *     <li>DELETE /workspaces/remove/{id} - Delete a workspace</li>
 * </ul>
 */
@RestController
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    /**
     * Fetch all workspaces.
     *
     * @return List of all workspaces.
     */
    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<List<WorkspaceResponseDTO>> getAllWorkspaces() {
        List<Workspace> workspaces = workspaceService.findAll();
        List<WorkspaceResponseDTO> workspaceResponseDTOs = WorkspaceConverter.toDTO(workspaces);

        return ResponseEntity.ok(workspaceResponseDTOs);
    }

    /**
     * Fetch a workspace by its ID.
     *
     * @param id the ID of the workspace to fetch
     * @return the {@link WorkspaceResponseDTO} corresponding to the workspace, or 404 if not found.
     */
    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<WorkspaceResponseDTO> getWorkspaceById(@PathVariable("id") Long id) {
        Optional<Workspace> workspace = workspaceService.findById(id);
        return workspace
                .map(w -> ResponseEntity.ok(WorkspaceConverter.toDTO(w)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Create a new workspace.
     *
     * @param request the details of the workspace to be created
     * @return the created workspace with HTTP 201 status.
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<WorkspaceRequestDTO> createWorkspace(@RequestBody WorkspaceRequestDTO request) {
        Workspace workspace = WorkspaceConverter.toEntity(request);
        workspaceService.save(workspace);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    /**
     * Delete a workspace by its ID.
     *
     * @param id the ID of the workspace to delete
     * @return HTTP 204 No Content if the workspace is deleted successfully,
     *         404 if the workspace is not found, or 500 for other errors.
     */
    @DeleteMapping("remove/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable("id") Long id) {
        workspaceService.deleteById(id);
        return ResponseEntity.noContent().build();

    }
}
