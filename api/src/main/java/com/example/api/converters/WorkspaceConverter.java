package com.example.api.converters;

import com.example.api.dto.requests.WorkspaceRequestDTO;
import com.example.api.dto.responses.WorkspaceResponseDTO;
import com.example.domain.entities.Workspace;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class WorkspaceConverter {

    private static final ModelMapper modelMapper = new ModelMapper();


    public static Workspace toEntity(WorkspaceRequestDTO dto) {
        return modelMapper.map(dto, Workspace.class);
    }

    public static WorkspaceResponseDTO toDTO(Workspace entity) {
        WorkspaceResponseDTO dto = modelMapper.map(entity, WorkspaceResponseDTO.class);
        dto.setReservations(ReservationConverter.toDTO(entity.getReservations()));
                return dto;
    }

    public static List<WorkspaceResponseDTO> toDTO(List<Workspace> workspaces) {
        return workspaces.stream()
                .map(WorkspaceConverter::toDTO)
                .collect(Collectors.toList());
    }
}
