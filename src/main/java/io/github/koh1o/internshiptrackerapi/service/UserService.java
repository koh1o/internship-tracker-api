package io.github.koh1o.internshiptrackerapi.service;

import io.github.koh1o.internshiptrackerapi.entity.User;
import io.github.koh1o.internshiptrackerapi.exception.EmailAlreadyExistsException;
import io.github.koh1o.internshiptrackerapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        String passwordHash = passwordEncoder.encode(password);

        User user = new User(email, passwordHash);

        return userRepository.save(user);
    }
}
