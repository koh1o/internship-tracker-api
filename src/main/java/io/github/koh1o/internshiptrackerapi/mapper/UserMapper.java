package io.github.koh1o.internshiptrackerapi.mapper;

import io.github.koh1o.internshiptrackerapi.dto.auth.UserResponse;
import io.github.koh1o.internshiptrackerapi.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
