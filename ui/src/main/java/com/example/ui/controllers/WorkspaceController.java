package com.example.ui.controllers;

import com.example.entities.Workspace;
import com.example.services.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing workspaces.
 * Provides endpoints to retrieve, create, and delete workspaces.
 *
 * <p>Endpoints:</p>
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
    public ResponseEntity<List<Workspace>> getAllWorkspaces() {
        List<Workspace> workspaces = workspaceService.getAll();
        return ResponseEntity.ok(workspaces);
    }

    /**
     * Fetch a workspace by ID.
     *
     * @param id Workspace ID.
     * @return Workspace if found, 404 otherwise.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Workspace> getWorkspaceById(@PathVariable("id") Long id) {
        Optional<Workspace> workspace = workspaceService.getById(id);
        return workspace.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Create a new workspace.
     *
     * @param workspace Workspace details.
     * @return Created workspace with 201 status.
     */
    @PostMapping("/create")
    public ResponseEntity<Workspace> createWorkspace(@RequestBody Workspace workspace) {
        workspaceService.create(workspace);
        return ResponseEntity.status(HttpStatus.CREATED).body(workspace);
    }

    /**
     * Delete a workspace by ID.
     *
     * @param id Workspace ID.
     * @return 204 No Content if successful, 404 if not found, 500 for other errors.
     */
    @DeleteMapping("remove/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable("id") Long id) {
        workspaceService.remove(id);
        return ResponseEntity.noContent().build();

    }
}
