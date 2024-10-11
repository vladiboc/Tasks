package org.example.tasks.web.v1.dto;

/**
 * DTO для ответа. Сущность пользователя.
 */
public record UserResponse(
    String id,
    String name,
    String email
) {
}
