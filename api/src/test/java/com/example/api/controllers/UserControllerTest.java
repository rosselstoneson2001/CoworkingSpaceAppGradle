package com.example.api.controllers;

import com.example.api.controllers.config.TestSecurityConfig;
import com.example.api.dto.requests.UserRequestDTO;
import com.example.api.dto.responses.UserResponseDTO;
import com.example.domain.entities.User;
import com.example.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;

    private User user;

    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        user = new User();
        user.setUserId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUserId(user.getUserId());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setEmail(user.getEmail());
    }

    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    @Test
    void getAllUsers_ShouldReturnUserList() throws Exception {
        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/users/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(userResponseDTO.getUserId()))
                .andExpect(jsonPath("$[0].firstName").value(userResponseDTO.getFirstName()))
                .andExpect(jsonPath("$[0].email").value(userResponseDTO.getEmail()));
    }

    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userResponseDTO.getUserId()))
                .andExpect(jsonPath("$.firstName").value(userResponseDTO.getFirstName()));
    }

    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    @Test
    void getUserById_UserNotFound_ShouldReturn404() throws Exception {
        when(userService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/get/1"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    @Test
    void createUser_ShouldReturn201() throws Exception {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setPassword("password123");

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).save(any(User.class));
    }

    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    @Test
    void deleteUser_ShouldReturn204() throws Exception {
        doNothing().when(userService).deleteById(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    @Test
    void checkPassword_Valid_ShouldReturnTrue() throws Exception {
        when(userService.checkPassword(1L, "password123")).thenReturn(true);

        mockMvc.perform(post("/users/check-password")
                        .param("userId", "1")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    @Test
    void checkPassword_Invalid_ShouldReturnForbidden() throws Exception {
        when(userService.checkPassword(1L, "wrongpassword")).thenReturn(false);

        mockMvc.perform(post("/users/check-password")
                        .param("userId", "1")
                        .param("password", "wrongpassword"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("false"));
    }

    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    @Test
    void deleteUser_WhenException_ShouldReturn404() throws Exception {
        doThrow(new RuntimeException()).when(userService).deleteById(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound());
    }


}