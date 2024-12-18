package org.example.tasks.web.v1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tasks.mapper.v1.TaskMapper;
import org.example.tasks.service.TaskService;
import org.example.tasks.web.v1.dto.TaskResponse;
import org.example.tasks.web.v1.dto.TaskUpsertRequest;
import org.example.tasks.web.v1.validation.UuidValid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Контроллер управления задачами.
 */
@RestController
@RequestMapping("/api/v1/tasks")
//@Loggable // Отключил чтобы тесты проходили, интерсепторы не дружат
@Validated
@RequiredArgsConstructor
@Tag(name = "Сервис задач", description = "CRUD сервис управления задачами.")
public class TaskController {
  private final TaskService taskService;
  private final TaskMapper taskMapper;

  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  @GetMapping
  public Flux<TaskResponse> findAll() {
    return this.taskService.findAll().map(this.taskMapper::taskToTaskResponse);
  }

  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  @GetMapping("/{id}")
  public Mono<ResponseEntity<TaskResponse>> findById(@PathVariable @UuidValid final String id) {
    return this.taskService.findById(id)
        .map(this.taskMapper::taskToTaskResponse)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  @PostMapping
  @PreAuthorize("hasRole('MANAGER')")
  public Mono<ResponseEntity<TaskResponse>> create(
      @RequestBody @Valid final TaskUpsertRequest request
  ) {
    return this.taskService.create(this.taskMapper.requestToTask(request))
        .map(this.taskMapper::taskToTaskResponse)
        .map(taskResponse -> ResponseEntity.status(HttpStatus.CREATED).body(taskResponse));
  }

  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('MANAGER')")
  public Mono<ResponseEntity<TaskResponse>> update(
      @PathVariable @UuidValid final String id, @RequestBody @Valid final TaskUpsertRequest request
  ) {
    return this.taskService.update(id, this.taskMapper.requestToTask(request))
        .map(this.taskMapper::taskToTaskResponse)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  @PatchMapping("/{taskId}/{observerId}")
  public Mono<ResponseEntity<TaskResponse>> addObserver(
      @PathVariable @UuidValid final String taskId, @PathVariable @UuidValid final String observerId
  ) {
    return this.taskService.addObserver(taskId, observerId)
        .map(this.taskMapper::taskToTaskResponse)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('MANAGER')")
  public Mono<ResponseEntity<Void>> deleteById(@PathVariable @UuidValid final String id) {
    return this.taskService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
  }
}
