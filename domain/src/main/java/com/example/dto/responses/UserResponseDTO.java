package com.example.dto.responses;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;

}
