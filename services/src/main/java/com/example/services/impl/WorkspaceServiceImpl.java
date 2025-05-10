package com.example.services.impl;


import com.example.entities.Workspace;
import com.example.exceptions.InvalidWorkspaceException;
import com.example.exceptions.enums.ErrorCodes;
import com.example.repositories.WorkspaceRepository;
import com.example.services.WorkspaceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }


    @Override
    public void create(Workspace workspace) {
        if (workspace.getType().isEmpty()) {
            throw new InvalidWorkspaceException(ErrorCodes.INVALID_WORKSPACE, "Workspace type is required.");
        }
        if (workspace.getPrice() == null || workspace.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidWorkspaceException(ErrorCodes.INVALID_WORKSPACE, "Workspace price is required.");
        }
        workspaceRepository.add(workspace);
    }


    @Override
    public List<Workspace> getAll() {
        List<Workspace> workspaces = workspaceRepository.getAll();
        if (workspaces.isEmpty()) {
            throw new InvalidWorkspaceException(ErrorCodes.INVALID_WORKSPACE, "No workspace found.");
        } else {
            return workspaces;
        }
    }

    @Override
    public Optional<Workspace> getById(Long workspaceId) {
        return workspaceRepository.getById(workspaceId);
    }

    @Override
    public void remove(Long workspaceId) {
        workspaceRepository.remove(workspaceId);
    }


//    @Override
//    public void save() {
//        workspaceRepository.save();
//    }
//
//    @Override
//    public void load() {
//        workspaceRepository.load();
//    }


}
