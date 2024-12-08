package org.example.tasks.web.v1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import org.example.tasks.constant.ErrorMsg;
import org.example.tasks.dao.entity.RoleType;
import org.example.tasks.web.v1.validation.EnumValid;

/**
 * DTO для создания/обновления пользователя.
 */
public record UserUpsertRequest(
    @NotBlank(message = ErrorMsg.CANNOT_BE_BLANK)
    String name,
    @NotBlank(message = ErrorMsg.CANNOT_BE_BLANK)
    String email,
    @NotBlank(message = ErrorMsg.CANNOT_BE_BLANK)
    String password,
    @NotNull(message = ErrorMsg.CANNOT_BE_BLANK)
    Set<RoleType> roles
) {
}
