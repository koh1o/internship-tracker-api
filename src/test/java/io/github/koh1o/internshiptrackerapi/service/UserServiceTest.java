package io.github.koh1o.internshiptrackerapi.service;

import io.github.koh1o.internshiptrackerapi.entity.User;
import io.github.koh1o.internshiptrackerapi.exception.EmailAlreadyExistsException;
import io.github.koh1o.internshiptrackerapi.exception.InvalidCredentialsException;
import io.github.koh1o.internshiptrackerapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

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

    @Test
    void shouldAuthenticateUser() {
        String email = "student@example.com";
        String password = "password123";

        User user = new User(email, "encodedPassword");

        UsernamePasswordAuthenticationToken authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(
                        email,
                        password
                );

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenReturn(authentication);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        User authenticatedUser = userService.authenticate(email, password);

        assertSame(user, authenticatedUser);

        verify(authenticationManager).authenticate(authenticationRequest);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldRejectAuthenticationWhenCredentialsAreInvalid() {
        String email = "student@example.com";
        String rawPassword = "wrongPassword";

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(
                InvalidCredentialsException.class,
                () -> userService.authenticate(email, rawPassword));
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldRejectAuthenticationWhenUserCannotBeLoadedAfterAuthentication() {
        String email = "student@example.com";
        String rawPassword = "secret123";

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenReturn(authentication);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> userService.authenticate(email, rawPassword)
        );

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(userRepository).findByEmail(email);
    }
}
