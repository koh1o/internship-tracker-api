package io.github.koh1o.internshiptrackerapi.controller;

import io.github.koh1o.internshiptrackerapi.dto.auth.RegistrationRequest;
import io.github.koh1o.internshiptrackerapi.dto.auth.UserResponse;
import io.github.koh1o.internshiptrackerapi.entity.User;
import io.github.koh1o.internshiptrackerapi.mapper.UserMapper;
import io.github.koh1o.internshiptrackerapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;

    public AuthController(
            UserService userService,
            UserMapper userMapper
    ) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegistrationRequest request
    ) {
        User user = userService.register(request.email(), request.password());
        UserResponse userResponse = userMapper.toResponse(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}
