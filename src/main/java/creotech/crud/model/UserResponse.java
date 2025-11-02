package creotech.crud.model;

import java.time.LocalDateTime;

public record UserResponse(
        String uuid,
        String email,
        String name,
        String token,
        Long tokenExpiredAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
