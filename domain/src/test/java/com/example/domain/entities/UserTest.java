package com.example.domain.entities;

import com.example.domain.entities.security.RoleEntity;
import com.example.domain.entities.security.enums.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setPassword("securepassword");
    }

    @Test
    void testConstructorAndGetters() {
        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity role = new RoleEntity();
        role.setId(1L);
        role.setName(RoleName.ROLE_CUSTOMER);
        roles.add(role);

        User newUser = new User(
                2L,
                "Jane",
                "Smith",
                "jane@example.com",
                "password123",
                true,
                roles
        );

        assertEquals(2L, newUser.getUserId());
        assertEquals("Jane", newUser.getFirstName());
        assertEquals("Smith", newUser.getLastName());
        assertEquals("jane@example.com", newUser.getEmail());
        assertEquals("password123", newUser.getPassword());
        assertTrue(newUser.isActive());
        assertEquals(1, newUser.getRoles().size());
        assertTrue(newUser.getRoles().contains(role));
    }

    @Test
    void testSetters() {
        user.setFirstName("Michael");
        user.setLastName("Jordan");
        user.setEmail("mj@example.com");
        user.setPassword("newpassword");
        user.setActive(false);

        assertEquals("Michael", user.getFirstName());
        assertEquals("Jordan", user.getLastName());
        assertEquals("mj@example.com", user.getEmail());
        assertEquals("newpassword", user.getPassword());
        assertFalse(user.isActive());
    }

    @Test
    void testDefaultValues() {
        assertTrue(user.isActive());
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
    }

    @Test
    void testRoleAssociation() {
        RoleEntity adminRole = new RoleEntity();
        adminRole.setId(1L);
        adminRole.setName(RoleName.ROLE_ADMIN);

        user.getRoles().add(adminRole);

        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(adminRole));
    }
}
