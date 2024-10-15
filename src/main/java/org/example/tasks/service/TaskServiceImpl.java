package org.example.tasks.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.tasks.dao.entity.Task;
import org.example.tasks.dao.entity.TaskStatus;
import org.example.tasks.dao.repository.TaskRepository;
import org.example.tasks.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Реклизация сервиса задач.
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
  private final UserService userService;
  private final TaskRepository taskRepository;

  @Override
  public Flux<Task> findAll() {
    Flux<Task> taskFlux = this.taskRepository.findAll();
    return taskFlux.flatMap(task ->
      Mono.zip(
          Mono.just(task),
          this.userService.findById(task.getAuthorId()),
          this.userService.findById(task.getAssigneeId()),
          this.userService.findAllById(task.getObserverIds()).collect(Collectors.toSet())
      ).map(tuple -> {
        tuple.getT1().setAuthor(tuple.getT2());
        tuple.getT1().setAssignee(tuple.getT3());
        tuple.getT1().setObservers(tuple.getT4());
        return tuple.getT1();
      }).map(List::of).flatMapMany(Flux::fromIterable)
    );
  }

  @Override
  public Mono<Task> findById(final String id) {
    final var taskMono = this.taskRepository.findById(id)
        .switchIfEmpty(Mono.error(new TaskNotFoundException(id)));
    return Mono.zip(
        taskMono,
        taskMono.flatMap(task -> this.userService.findById(task.getAuthorId())),
        taskMono.flatMap(task -> this.userService.findById(task.getAssigneeId())),
        taskMono.flatMap(task -> this.userService
            .findAllById(task.getObserverIds()).collect(Collectors.toSet()))
    ).map(tuple -> {
      tuple.getT1().setAuthor(tuple.getT2());
      tuple.getT1().setAssignee(tuple.getT3());
      tuple.getT1().setObservers(tuple.getT4());
      return tuple.getT1();
    });
  }

  @Override
  public Mono<Task> create(final Task newTask) {
    newTask.setId(UUID.randomUUID().toString());
    final var now = Instant.now();
    newTask.setCreatedAt(now);
    newTask.setUpdatedAt(now);
    return Mono.zip(
        Mono.just(newTask),
        this.userService.findById(newTask.getAuthorId()),
        this.userService.findById(newTask.getAssigneeId()),
        this.userService.findAllById(newTask.getObserverIds()).collect(Collectors.toSet())
    ).flatMap(tuple -> {
      tuple.getT1().setAuthor(tuple.getT2());
      tuple.getT1().setAssignee(tuple.getT3());
      tuple.getT1().setObservers(tuple.getT4());
      return this.taskRepository.save(tuple.getT1());
    });
  }

  @Override
  public Mono<Task> update(final String id, final Task updatedTask) {
    final var taskMono = this.taskRepository.findById(id)
        .switchIfEmpty(Mono.error(new TaskNotFoundException(id)));
    return taskMono.flatMap(task -> {
      task.setName(updatedTask.getName());
      task.setDescription(updatedTask.getDescription());
      task.setUpdatedAt(Instant.now());
      task.setStatus(updatedTask.getStatus());
      task.setAuthorId(updatedTask.getAuthorId());
      task.setAssigneeId(updatedTask.getAssigneeId());
      task.setObserverIds(updatedTask.getObserverIds());
      return this.taskRepository.save(task).flatMap(savedTask -> this.findById(savedTask.getId()));
    });
  }

  @Override
  public Mono<Task> addObserver(final String taskId, final String observerId) {
    final var taskMono = this.taskRepository.findById(taskId)
        .switchIfEmpty(Mono.error(new TaskNotFoundException(taskId)));
    return taskMono.flatMap(task -> {
      task.getObserverIds().add(observerId);
      return taskRepository.save(task).flatMap(savedTask -> this.findById(savedTask.getId()));
    });
  }

  @Override
  public Mono<Void> deleteById(final String id) {
    return this.taskRepository.deleteById(id);
  }
}
