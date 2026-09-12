package io.github.koh1o.internshiptrackerapi.service;

import io.github.koh1o.internshiptrackerapi.entity.User;
import io.github.koh1o.internshiptrackerapi.exception.EmailAlreadyExistsException;
import io.github.koh1o.internshiptrackerapi.exception.InvalidCredentialsException;
import io.github.koh1o.internshiptrackerapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User register(String email, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        String passwordHash = passwordEncoder.encode(password);

        User user = new User(email, passwordHash);

        return userRepository.save(user);
    }

    public User authenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(
                        email,
                        password
                );

        try {
            authenticationManager.authenticate(authenticationRequest);

        } catch (BadCredentialsException exception) {
            throw new InvalidCredentialsException();
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException());
    }
}
