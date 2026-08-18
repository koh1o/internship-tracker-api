package io.github.koh1o.internshiptrackerapi.controller;

import io.github.koh1o.internshiptrackerapi.dto.auth.UserResponse;
import io.github.koh1o.internshiptrackerapi.entity.User;
import io.github.koh1o.internshiptrackerapi.exception.EmailAlreadyExistsException;
import io.github.koh1o.internshiptrackerapi.mapper.UserMapper;
import io.github.koh1o.internshiptrackerapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @Test
    void shouldRegisterUser() throws Exception {
        String email = "student@example.com";
        String rawPassword = "secret123";
        String passwordHash = "encoded-password";

        LocalDateTime createdAt =
                LocalDateTime.of(2026, 8, 18, 18, 0);

        User user = new User(email, passwordHash);

        UserResponse response = new UserResponse(
                1L,
                email,
                createdAt,
                createdAt
        );

        when(userService.register(email, rawPassword))
                .thenReturn(user);

        when(userMapper.toResponse(user))
                .thenReturn(response);

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
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.passwordHash").doesNotExist());

        verify(userService).register(email, rawPassword);
        verify(userMapper).toResponse(user);
    }

    @Test
    void shouldRejectInvalidEmail() throws Exception {
        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "email": "not-an-email",
                                          "password": "secret123"
                                        }
                                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.email").value("Email must be valid"));

        verifyNoInteractions(userMapper, userService);
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        String email = "student@example.com";
        String rawPassword = "secret123";

        when(userService.register(email, rawPassword))
                .thenThrow(new EmailAlreadyExistsException(email));

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "email": "student@example.com",
                                          "password": "secret123"
                                        }
                                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("User with email '" + email + "' already exists"))
                .andExpect(jsonPath("$.path").value("/api/auth/register"));

        verify(userService).register(email, rawPassword);
        verifyNoInteractions(userMapper);
    }
}
