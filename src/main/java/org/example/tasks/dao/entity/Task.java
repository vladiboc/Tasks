package org.example.tasks.dao.entity;

import java.time.Instant;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class Task {
  @Id
  private String id;
  private String name;
  private String description;
  @CreatedDate
  private Instant createdAt;
  @LastModifiedDate
  private Instant updatedAt;
  private TaskStatus status;
  private String authorId;
  private String assigneeId;
  @Field("observerIds")
  private Set<String> observerIds;
  @ReadOnlyProperty
  private User author;
  @ReadOnlyProperty
  private User assignee;
  @ReadOnlyProperty
  private Set<User> observers;
}
