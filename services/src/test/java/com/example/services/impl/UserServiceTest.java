package com.example.services.impl;

import com.example.domain.entities.User;
import com.example.domain.exceptions.InvalidUserException;
import com.example.domain.exceptions.UserNotFoundException;
import com.example.domain.repositories.UserRepository;
import com.example.domain.utils.PasswordUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setPassword("hashedPassword123");
    }

    @Test
    void shouldSaveUserSuccessfully() {
        user.setPassword("plainPassword123");
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.save(user);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldFindAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> result = userService.findAll();
        assertEquals(1, result.size());
        assertEquals("john@example.com", result.get(0).getEmail());
    }

    @Test
    void shouldFindUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> result = userService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void shouldDeleteUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.deleteById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldCheckPasswordSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // simulate match
        mockStatic(PasswordUtils.class).when(() -> PasswordUtils.checkPassword("plain", "hashedPassword123")).thenReturn(true);
        assertTrue(userService.checkPassword(1L, "plain"));
    }

    @Test
    void shouldFindByEmailSuccessfully() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(user);
        User found = userService.findByEmail("john@example.com");
        assertEquals("john@example.com", found.getEmail());
    }

    // --- EXCEPTION CASES ---

    @Test
    void shouldThrowWhenSavingUserWithMissingFields() {
        User invalidUser = new User();
        Exception exception = assertThrows(InvalidUserException.class, () -> userService.save(invalidUser));
        assertTrue(exception.getMessage().contains("All user fields are required"));
    }

    @Test
    void shouldThrowWhenNoUsersFound() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(UserNotFoundException.class, () -> userService.findAll());
    }

    @Test
    void shouldThrowWhenUserNotFoundById() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findById(2L));
    }

    @Test
    void shouldThrowWhenDeletingNonexistentUser() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteById(99L));
    }

    @Test
    void shouldThrowWhenPasswordCheckFailsDueToMissingUser() {
        when(userRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.checkPassword(123L, "any"));
    }

    @Test
    void shouldThrowWhenUserNotFoundByEmail() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail("missing@example.com"));
    }
}
