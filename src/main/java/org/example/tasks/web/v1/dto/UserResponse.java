package org.example.tasks.web.v1.dto;

import java.util.Set;
import org.example.tasks.dao.entity.RoleType;

/**
 * DTO для ответа. Сущность пользователя.
 */
public record UserResponse(
    String id,
    String name,
    String email,
    Set<RoleType> roles
) {
}
