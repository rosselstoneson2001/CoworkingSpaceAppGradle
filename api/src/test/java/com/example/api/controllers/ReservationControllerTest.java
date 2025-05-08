package com.example.api.controllers;

import com.example.api.controllers.config.TestSecurityConfig;
import com.example.api.dto.requests.ReservationRequestDTO;
import com.example.api.dto.responses.ReservationResponseDTO;
import com.example.api.facade.ReservationFacade;
import com.example.domain.entities.Reservation;
import com.example.domain.entities.Workspace;
import com.example.domain.exceptions.WorkspaceNotFoundException;
import com.example.domain.exceptions.enums.NotFoundErrorCodes;
import com.example.services.ReservationService;
import com.example.services.UserService;
import com.example.services.WorkspaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(ReservationController.class)
@Import({TestSecurityConfig.class})
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private WorkspaceService workspaceService;

    @MockBean
    private ReservationFacade reservationFacade;

    @MockBean
    private UserService userService;

    private ReservationRequestDTO reservationRequestDTO;
    private ReservationResponseDTO reservationResponseDTO;
    private Reservation reservation;
    private Workspace workspace;

    @BeforeEach
    void setUp() {

        workspace = new Workspace(1L, "Private", null, true, null);
        reservationRequestDTO = new ReservationRequestDTO(1L, 1L, "John Doe", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        reservationResponseDTO = new ReservationResponseDTO(1L, "John Doe", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        reservation = new Reservation(1L, "John Doe", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), null, true, workspace, null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllReservations() throws Exception {
        when(reservationService.findAll()).thenReturn(Arrays.asList(reservation));
        when(reservationService.findReservationsByCustomer("John Doe")).thenReturn(Arrays.asList(reservation));

        mockMvc.perform(get("/reservations/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("John Doe"))
                .andExpect(jsonPath("$[0].startDateTime").exists())
                .andExpect(jsonPath("$[0].endDateTime").exists());
    }

    @Test
    @WithMockUser(authorities = "READ")
    void testGetReservationById() throws Exception {
        when(reservationService.findById(1L)).thenReturn(Optional.of(reservation));

        mockMvc.perform(get("/reservations/get/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.startDateTime").exists())
                .andExpect(jsonPath("$.endDateTime").exists());
    }

    @Test
    @WithMockUser(authorities = "READ")
    void testCreateReservation() throws Exception {

        com.example.domain.entities.User user = new com.example.domain.entities.User();
        user.setUserId(1L);
        user.setEmail("johndoe@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");

        when(reservationFacade.createReservation(any(ReservationRequestDTO.class))).thenReturn(reservationResponseDTO);

        mockMvc.perform(post("/reservations/create")
                        .contentType("application/json")
                        .content("{ \"workspaceId\": 1, \"userId\": 1, \"customerName\": \"John Doe\", \"startDateTime\": \"2025-04-10T10:00:00\", \"endDateTime\": \"2025-04-10T12:00:00\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("John Doe"));
    }

    @Test
    @WithMockUser(authorities = "READ")
    void testCreateReservation_WorkspaceNotFound() throws Exception {
        when(workspaceService.findById(1L)).thenReturn(Optional.empty());
        when(reservationFacade.createReservation(any(ReservationRequestDTO.class)))
                .thenThrow(new WorkspaceNotFoundException(NotFoundErrorCodes.WORKSPACE_NOT_FOUND, "Workspace not found with ID: 1"));

        mockMvc.perform(post("/reservations/create")
                        .contentType("application/json")
                        .content("{ \"workspaceId\": 1, \"customerName\": \"John Doe\", \"startDateTime\": \"2025-04-10T10:00:00\", \"endDateTime\": \"2025-04-10T12:00:00\" }"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "READ")
    void testDeleteReservation() throws Exception {
        when(reservationService.findById(1L)).thenReturn(Optional.of(reservation));

        mockMvc.perform(delete("/reservations/remove/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetReservationsByCustomer() throws Exception {
        when(reservationService.findReservationsByCustomer("John Doe")).thenReturn(Arrays.asList(reservation));

        mockMvc.perform(get("/reservations/get/customer/{customerName}", "John Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("John Doe"));
    }

    @Test
    @WithMockUser(username = "JohnDoe", roles = "CUSTOMER")
    void testGetReservationsByCustomerId() throws Exception {
        User user = new User("JohnDoe", "password", List.of());
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(reservationService.findReservationsByCustomerEmail("JohnDoe")).thenReturn(Arrays.asList(reservation));

        mockMvc.perform(get("/reservations/get/customer-email/reservation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("John Doe"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetReservationsByWorkspace() throws Exception {
        when(reservationService.findReservationsByWorkspace(1L)).thenReturn(Arrays.asList(reservation));

        mockMvc.perform(get("/reservations/get/workspace/{workspaceId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("John Doe"));
    }


}
