package io.github.koh1o.internshiptrackerapi.security;

import io.github.koh1o.internshiptrackerapi.entity.User;
import io.github.koh1o.internshiptrackerapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void shouldLoadUserByUsername() {
        String email = "student@example.com";
        String passwordHash = "$2a$10$testHash";

        User user = new User(email, passwordHash);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        assertEquals(email, userDetails.getUsername());
        assertEquals(passwordHash, userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(
                userDetails.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("USER"))
        );

        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        String email = "missing@example.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(email)
        );

        verify(userRepository).findByEmail(email);
    }
}