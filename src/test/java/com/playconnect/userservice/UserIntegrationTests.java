package com.playconnect.userservice;

import com.playconnect.userservice.config.JWTService;
import com.playconnect.userservice.model.Role;
import com.playconnect.userservice.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTService jwtService;

    @PersistenceContext
    private EntityManager entityManager;

    private String user1Token;
    private String user2Token;
    private Long user1Id;
    private Long user2Id;

    @BeforeEach
    public void setup() {
        // Create two users and persist them
        User user1 = User.builder()
                .email("user1@example.com")
                .firstName("First")
                .lastName("User")
                .password("password")
                .role(Role.USER)
                .build();
        User user2 = User.builder()
                .email("user2@example.com")
                .firstName("Second")
                .lastName("User")
                .password("password")
                .role(Role.USER)
                .build();
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();
        user1Id = user1.getUserId();
        user2Id = user2.getUserId();

        // Generate JWT tokens for both users
        user1Token = jwtService.generateToken(user1);
        user2Token = jwtService.generateToken(user2);
    }

    @Test
    public void getUserById_ValidTokenMatchingId_ShouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/user/{userId}", user1Id)
                        .header("Authorization", "Bearer " + user1Token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }

    @Test
    public void getUserById_ValidTokenNotMatchingId_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/user/{userId}", user1Id)
                        .header("Authorization", "Bearer " + user2Token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("User not authorized to access this resource"));
    }

    @Test
    public void updateUser_ValidTokenMatchingId_ShouldUpdateUser() throws Exception {
        mockMvc.perform(put("/api/user/{userId}", user1Id)
                        .header("Authorization", "Bearer " + user1Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Updated\", \"lastName\":\"Name\", \"email\":\"user1@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    public void updateUser_ValidTokenNotMatchingId_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(put("/api/user/{userId}", user1Id)
                        .header("Authorization", "Bearer " + user2Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Unauthorized\", \"lastName\":\"Update\", \"email\":\"user1@example.com\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("User not authorized to access this resource"));
    }

    @Test
    public void deleteUser_ValidTokenMatchingId_ShouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/user/{userId}", user1Id)
                        .header("Authorization", "Bearer " + user1Token))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    public void deleteUser_ValidTokenNotMatchingId_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(delete("/api/user/{userId}", user1Id)
                        .header("Authorization", "Bearer " + user2Token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("User not authorized to access this resource"));
    }
}
