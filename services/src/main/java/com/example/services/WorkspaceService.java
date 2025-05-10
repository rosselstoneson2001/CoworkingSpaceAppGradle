package com.example.services;


import com.example.entities.Workspace;
import com.example.repositories.StorageManagerRepository;

public interface WorkspaceService extends CrudService<Workspace, Long>, StorageManagerRepository {

}
