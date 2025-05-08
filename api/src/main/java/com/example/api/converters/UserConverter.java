package com.example.api.converters;

import com.example.api.dto.requests.UserRequestDTO;
import com.example.api.dto.responses.UserResponseDTO;
import com.example.domain.entities.User;
import com.example.domain.utils.PasswordUtils;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static User toEntity(UserRequestDTO dto) {
        User user = modelMapper.map(dto, User.class);
        user.setPassword(PasswordUtils.hashPassword(dto.getPassword()));
        return user;
    }

    public static UserResponseDTO toDTO(User user) {
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public static List<UserResponseDTO> toDTO(List<User> users) {
        return users.stream()
                .map(UserConverter::toDTO)
                .collect(Collectors.toList());
    }
}
