package org.example.tasks.web.v1.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import org.example.tasks.constant.ErrorMsg;
import org.example.tasks.dao.entity.TaskStatus;
import org.example.tasks.web.v1.validation.EnumValid;
import org.example.tasks.web.v1.validation.UuidValid;

/**
 * Объект запроса от пользователя для создания/изменения задачи.
 */
public record TaskUpsertRequest(
    @NotBlank(message = ErrorMsg.CANNOT_BE_BLANK)
    String name,
    String description,
    @EnumValid(enumClass = TaskStatus.class)
    String status,
    @UuidValid
    String authorId,
    @UuidValid
    String assigneeId,
    Set<@UuidValid String> observerIds
) {
}
