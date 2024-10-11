package org.example.tasks.web.v1.dto;

import java.util.Set;
import org.example.tasks.dao.entity.TaskStatus;

public record TaskUpsertRequest(
    String name,
    String description,
    TaskStatus status,
    String authorId,
    String assigneeId,
    Set<String> observerIds
) {
}
