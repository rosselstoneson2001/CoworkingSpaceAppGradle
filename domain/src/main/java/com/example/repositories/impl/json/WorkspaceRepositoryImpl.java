package com.example.repositories.impl.json;

import com.example.entities.Workspace;
import com.example.exceptions.WorkspaceNotFoundException;
import com.example.exceptions.enums.ErrorCodes;
import com.example.repositories.DataStorage;
import com.example.repositories.StorageManagerRepository;
import com.example.repositories.WorkspaceRepository;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkspaceRepositoryImpl implements WorkspaceRepository, StorageManagerRepository {

    private List<Workspace> workspaces;
    private DataStorage<Workspace> dataStorage;

    public WorkspaceRepositoryImpl() {
    }

    @Override
    public void add(Workspace workspace) {
        workspace.setWorkspaceId(generateId());
        workspaces.add(workspace);
    }

    @Override
    public List<Workspace> getAll() {
        return new ArrayList<>(workspaces);
    }

    @Override
    public Optional<Workspace> getById(Long workspaceId) {
        return workspaces.stream()
                .filter(w -> w.getWorkspaceId().equals(workspaceId))
                .findFirst();
    }


    @Override
    public void remove(Long workspaceId) {
        boolean removed = workspaces.removeIf(w -> w.getWorkspaceId().equals(workspaceId));
        if (!removed) {
            throw new WorkspaceNotFoundException(ErrorCodes.WORKSPACE_NOT_FOUND, "No workspace found with ID: " + workspaceId);
        }
    }

    @Override
    public void save() {
        dataStorage.save(workspaces);
    }


    @Override
    public void load() {
        this.dataStorage = new DataStorageImpl<>(
                "workspaces.json",
                new TypeReference<>() {
                }
        );
        this.workspaces = dataStorage.load(); // Load the workspaces into the list

    }
}
