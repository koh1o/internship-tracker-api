package io.github.koh1o.internshiptrackerapi.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordConfigurationTest {

    private final PasswordEncoder passwordEncoder =
            new PasswordConfiguration().passwordEncoder();

    @Test
    void shouldEncodePassword() {
        String rawPassword = "secret123";

        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        assertFalse(passwordEncoder.matches("wrongPassword", encodedPassword));
    }

    @Test
    void shouldGenerateDifferentHashesForSamePassword() {
        String rawPassword = "secret123";

        String firstHash = passwordEncoder.encode(rawPassword);
        String secondHash = passwordEncoder.encode(rawPassword);

        assertNotEquals(firstHash, secondHash);
        assertTrue(passwordEncoder.matches(rawPassword, firstHash));
        assertTrue(passwordEncoder.matches(rawPassword, secondHash));
    }
}
