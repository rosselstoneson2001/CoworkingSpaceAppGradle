package com.example.converters;

import com.example.dto.requests.WorkspaceRequestDTO;
import com.example.dto.responses.WorkspaceResponseDTO;
import com.example.entities.Workspace;

import java.util.List;
import java.util.stream.Collectors;

public class WorkspaceConverter {

    public static Workspace toEntity(WorkspaceRequestDTO dto) {
        Workspace workspace = new Workspace();

        workspace.setType(dto.getType());
        workspace.setPrice(dto.getPrice());

        return workspace;
    }

    public static WorkspaceResponseDTO toDTO(Workspace entity) {
        WorkspaceResponseDTO dto = new WorkspaceResponseDTO();

        dto.setWorkspaceId(entity.getWorkspaceId());
        dto.setType(entity.getType());
        dto.setPrice(entity.getPrice());
        dto.setReservations(ReservationConverter.toDTO(entity.getReservations()));

        return dto;
    }

    public static List<WorkspaceResponseDTO> toDTO(List<Workspace> workspaces) {
        return workspaces.stream()
                .map(WorkspaceConverter::toDTO)
                .collect(Collectors.toList());
    }
}
