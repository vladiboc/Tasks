package org.example.tasks.web.v1.dto;

import java.time.Instant;
import java.util.Set;
import org.example.tasks.dao.entity.TaskStatus;
import org.example.tasks.dao.entity.User;

public record TaskResponse(
    String id,
    String name,
    String description,
    Instant createdAt,
    Instant updatedAt,
    TaskStatus status,
    User author,
    User assignee,
    Set<User> observers
) {
}
