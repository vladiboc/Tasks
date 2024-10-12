package org.example.tasks.web.v1.dto;

import jakarta.validation.constraints.NotBlank;
import org.example.tasks.constant.ErrorMsg;

/**
 * DTO для создания/обновления пользователя.
 */
public record UserUpsertRequest(
    @NotBlank(message = ErrorMsg.CANNOT_BE_BLANK)
    String name,
    @NotBlank(message = ErrorMsg.CANNOT_BE_BLANK)
    String email
) {
}
