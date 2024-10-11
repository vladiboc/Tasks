package org.example.tasks.web.v1.dto;

/**
 * DTO для создания/обновления пользователя.
 */
public record UserUpsertRequest(
    String name,
    String email
) {
}
