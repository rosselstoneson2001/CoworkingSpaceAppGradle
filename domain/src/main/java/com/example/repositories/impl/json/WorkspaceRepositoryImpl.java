package com.example.repositories.impl.json;

import com.example.entities.Workspace;
import com.example.exceptions.WorkspaceNotFoundException;
import com.example.exceptions.enums.NotFoundErrorCodes;
import com.example.repositories.DataStorage;
import com.example.repositories.StorageManagerRepository;
import com.example.repositories.WorkspaceRepository;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link WorkspaceRepository} that provides data storage and retrieval for workspaces.
 * It uses a list to store workspaces and saves data to and loads data from a JSON file.
 * Implements {@link StorageManagerRepository} to handle data persistence operations.
 */
public class WorkspaceRepositoryImpl implements WorkspaceRepository, StorageManagerRepository {

    private List<Workspace> workspaces;
    private DataStorage<Workspace> dataStorage;

    public WorkspaceRepositoryImpl() {
    }

    /**
     * Adds a workspace to the repository. The workspace ID is generated automatically.
     *
     * @param workspace The workspace to be added to the repository.
     */
    @Override
    public void add(Workspace workspace) {
        workspace.setWorkspaceId(generateId());
        workspaces.add(workspace);
    }

    /**
     * Retrieves all workspaces from the repository.
     *
     * @return A list of all workspaces stored in the repository.
     */
    @Override
    public List<Workspace> getAll() {
        return new ArrayList<>(workspaces);
    }

    /**
     * Retrieves a workspace by its ID.
     *
     * @param workspaceId The ID of the workspace to be retrieved.
     * @return An {@link Optional} containing the workspace if found, or empty if not found.
     */
    @Override
    public Optional<Workspace> getById(Long workspaceId) {
        return workspaces.stream()
                .filter(w -> w.getWorkspaceId().equals(workspaceId))
                .findFirst();
    }

    /**
     * Removes a workspace by its ID.
     * Throws a {@link WorkspaceNotFoundException} if no workspace is found with the given ID.
     *
     * @param workspaceId The ID of the workspace to be removed.
     * @throws WorkspaceNotFoundException if no workspace is found with the given ID.
     */
    @Override
    public void remove(Long workspaceId) {
        boolean removed = workspaces.removeIf(w -> w.getWorkspaceId().equals(workspaceId));
        if (!removed) {
            throw new WorkspaceNotFoundException(NotFoundErrorCodes.WORKSPACE_NOT_FOUND, "Failed to delete Workspace. No workspace found with ID: " + workspaceId);
        }
    }

    /**
     * Saves the current state of the workspaces to a JSON file.
     * The workspaces are serialized and written to the file defined in the data storage.
     */
    @Override
    public void save() {
        dataStorage.save(workspaces);
    }

    /**
     * Loads the workspaces from a JSON file into the repository.
     * Initializes the {@link DataStorage} and reads the data from the file.
     */
    @Override
    public void load() {
        this.dataStorage = new DataStorageImpl<>(
                "workspaces.json",
                new TypeReference<>() {
                }
        );
        this.workspaces = dataStorage.load(); // Load the workspaces into the list

    }


    @Override
    public Workspace getWorkspaceWithReservations(Long id) {
        return null;
    }
}
