package org.example.tasks.web.v1.dto;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import org.example.tasks.dao.entity.TaskStatus;
import org.example.tasks.dao.entity.User;

/**
 * Объект "задача" для ответа пользователю.
 */
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

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    return (o instanceof final TaskResponse other)
        && Objects.equals(this.id, other.id)
        && Objects.equals(this.name, other.name)
        && Objects.equals(this.description, other.description)
        && Objects.equals(this.status, other.status)
        && Objects.equals(this.author, other.author)
        && Objects.equals(this.assignee, other.assignee)
        && Objects.equals(this.observers, other.observers);
  }
}
