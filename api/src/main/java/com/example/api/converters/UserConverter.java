package com.example.api.converters;

import com.example.api.dto.requests.UserRequestDTO;
import com.example.api.dto.responses.UserResponseDTO;
import com.example.domain.entities.User;
import com.example.domain.utils.PasswordUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

    public static User toEntity(UserRequestDTO dto) {
        User user = new User();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(PasswordUtils.hashPassword(dto.getPassword()));

        return user;
    }

    public static UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();

        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());

        return dto;
    }

    public static List<UserResponseDTO> toDTO(List<User> users) {
        return users.stream()
                .map(UserConverter::toDTO)
                .collect(Collectors.toList());
    }
}
