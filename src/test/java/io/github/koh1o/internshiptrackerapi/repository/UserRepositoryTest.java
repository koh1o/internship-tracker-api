package io.github.koh1o.internshiptrackerapi.repository;

import io.github.koh1o.internshiptrackerapi.configuration.TestcontainersConfiguration;
import io.github.koh1o.internshiptrackerapi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByEmail() {
        User user = new User(
                "student@example.com",
                "some-password-hash"
        );

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
        assertTrue(foundUser.isPresent());
        User actualUser = foundUser.get();
        assertEquals(user.getEmail(), actualUser.getEmail());
        assertEquals(user.getPasswordHash(), actualUser.getPasswordHash());
    }

    @Test
    void shouldCheckIfUserExistsByEmail() {
        User user = new User(
                "student@example.com",
                "some-password-hash"
        );

        userRepository.save(user);

        assertTrue(userRepository.existsByEmail(user.getEmail()));
        assertFalse(userRepository.existsByEmail("other email"));
    }

    @Test
    void shouldRejectDuplicateEmail() {
        User firstUser = new User(
                "student@example.com",
                "first-hash"
        );

        User secondUser = new User(
                "student@example.com",
                "second-hash"
        );

        userRepository.saveAndFlush(firstUser);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> userRepository.saveAndFlush(secondUser)
        );
    }
}
