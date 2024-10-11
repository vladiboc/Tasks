package org.example.tasks.service;

import org.example.tasks.dao.entity.Task;
import org.springframework.data.domain.Range;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
  Flux<Task> findAll();

  Mono<Task> findById(String id);

  Mono<Task> create(Task newTask);

  Mono<Task> update(String id, Task updatedTask);

  Mono<Task> addObserver(String taskId, String observerId);

  Mono<Void> deleteById(String id);
}
