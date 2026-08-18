package io.github.koh1o.internshiptrackerapi.integration;

import io.github.koh1o.internshiptrackerapi.configuration.TestcontainersConfiguration;
import io.github.koh1o.internshiptrackerapi.entity.User;
import io.github.koh1o.internshiptrackerapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUserAndStoreEncodedPassword() throws Exception {
        String email = "student@example.com";
        String rawPassword = "secret123";

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "email": "student@example.com",
                                          "password": "secret123"
                                        }
                                        """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.passwordHash").doesNotExist());

        User savedUser = userRepository.findByEmail(email)
                .orElseThrow();

        assertEquals(email, savedUser.getEmail());

        assertNotEquals(rawPassword, savedUser.getPasswordHash());
        assertTrue(passwordEncoder.matches(rawPassword, savedUser.getPasswordHash()));
    }

    @Test
    void shouldReturnConflictForDuplicateEmail() throws Exception {
        String email = "student@example.com";

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "email": "student@example.com",
                                          "password": "secret123"
                                        }
                                        """)
                )
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "email": "student@example.com",
                                          "password": "anotherPassword"
                                        }
                                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("User with email '" + email + "' already exists"));

        assertEquals(1, userRepository.count());
    }
}
