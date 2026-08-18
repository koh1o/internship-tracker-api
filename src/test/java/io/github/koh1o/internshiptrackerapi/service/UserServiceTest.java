package io.github.koh1o.internshiptrackerapi.service;

import io.github.koh1o.internshiptrackerapi.entity.User;
import io.github.koh1o.internshiptrackerapi.exception.EmailAlreadyExistsException;
import io.github.koh1o.internshiptrackerapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUser() {
        String email = "student@example.com";
        String rawPassword = "secret123";
        String passwordHash = "encoded-password";

        when(userRepository.existsByEmail(email))
                .thenReturn(false);

        when(passwordEncoder.encode(rawPassword))
                .thenReturn(passwordHash);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User registeredUser = userService.register(email, rawPassword);
        assertEquals(email, registeredUser.getEmail());
        assertEquals(passwordHash, registeredUser.getPasswordHash());
    }

    @Test
    void shouldRejectDuplicateEmail() {
        String email = "student@example.com";
        String rawPassword = "secret123";

        when(userRepository.existsByEmail(email))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.register(email, rawPassword)
        );
    }

    @Test
    void shouldNotEncodePasswordWhenEmailAlreadyExists() {
        String email = "student@example.com";
        String rawPassword = "secret123";

        when(userRepository.existsByEmail(email))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.register(email, rawPassword)
        );

        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void shouldSaveEncodedPasswordInsteadOfRawPassword() {
        String email = "student@example.com";
        String rawPassword = "secret123";
        String passwordHash = "encoded-password";

        when(userRepository.existsByEmail(email))
                .thenReturn(false);

        when(passwordEncoder.encode(rawPassword))
                .thenReturn(passwordHash);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        userService.register(email, rawPassword);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(passwordHash, savedUser.getPasswordHash());
        assertNotEquals(rawPassword, savedUser.getPasswordHash());
    }
}
