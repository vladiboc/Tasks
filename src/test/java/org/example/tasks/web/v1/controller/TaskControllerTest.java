package org.example.tasks.web.v1.controller;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.example.tasks.AbstractTest;
import org.example.tasks.dao.entity.TaskStatus;
import org.example.tasks.dao.entity.User;
import org.example.tasks.web.v1.dto.TaskResponse;
import org.example.tasks.web.v1.dto.TaskUpsertRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class TaskControllerTest extends AbstractTest {
  private final User juan = new User(USER_JUAN_ID, "Хуан", "juan@mail.mx");
  private final User pedro = new User(USER_PEDRO_ID, "Педро", "pedro@mail.mx");
  private final User julio = new User(USER_JULIO_ID, "Хулио", "julio@mail.mx");

  @Test
  void whenGetAllTasks_thenReturnAllTasksFromDB() {
    final var expectedData = List.of(
        new TaskResponse(TASK_FIRST_ID, "Приготовить буррито", "Приготовить буррито с соусом сальса",
            CREATED_AT, UPDATED_AT, TaskStatus.TODO, juan, pedro, Set.of(julio)
        ),
        new TaskResponse(TASK_SECOND_ID, "Съесть буррито", "Скушать полностью буррито с соусом сальса",
            CREATED_AT, UPDATED_AT, TaskStatus.TODO, pedro, juan, Set.of(julio)
        )
    );
    System.out.println(this.webTestClient.get().uri("/api/v1/tasks")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(TaskResponse.class)
        .hasSize(2)
        .contains(expectedData.toArray(TaskResponse[]::new)));
  }

  @Test
  void whenGetById_thenReturnTaskById() {
    final var expectedData = new TaskResponse(
        TASK_FIRST_ID, "Приготовить буррито", "Приготовить буррито с соусом сальса",
        CREATED_AT, UPDATED_AT, TaskStatus.TODO, juan, pedro, Set.of(julio));

    this.webTestClient.get().uri("/api/v1/tasks/{id}", TASK_FIRST_ID)
        .exchange()
        .expectStatus().isOk()
        .expectBody(TaskResponse.class)
        .isEqualTo(expectedData);
  }

  @Test
  void whenCreateTask_thenReturnNewTask() {
    StepVerifier.create(taskRepository.count())
        .expectNext(2L)
        .expectComplete()
        .verify();

    final var requestedTask = new TaskUpsertRequest(
        "Доставить буррито", "Доставить буррито пока горячее",
        TaskStatus.TODO.toString(), USER_JUAN_ID, USER_JULIO_ID, Set.of(USER_PEDRO_ID));
    final var expectedData = new TaskResponse(
        UUID.randomUUID().toString(),
        "Доставить буррито", "Доставить буррито пока горячее",
        Instant.now(), Instant.now(), TaskStatus.TODO, juan, julio, Set.of(pedro));

    this.webTestClient.post().uri("/api/v1/tasks")
        .body(Mono.just(requestedTask), TaskUpsertRequest.class)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(TaskResponse.class)
        .value(taskResponse -> {
          Assertions.assertNotNull(taskResponse.id());
          Assertions.assertNotNull(taskResponse.createdAt());
          Assertions.assertNotNull(taskResponse.updatedAt());
          Assertions.assertEquals(expectedData.name(), taskResponse.name());
          Assertions.assertEquals(expectedData.description(), taskResponse.description());
          Assertions.assertEquals(expectedData.status(), taskResponse.status());
          Assertions.assertEquals(expectedData.author(), taskResponse.author());
          Assertions.assertEquals(expectedData.assignee(), taskResponse.assignee());
          Assertions.assertEquals(expectedData.observers(), taskResponse.observers());
        });

    StepVerifier.create(taskRepository.count())
        .expectNext(3L)
        .expectComplete()
        .verify();
  }

  @Test
  void whenUpdateTask_thenReturnNewTask() {
    final var requestedTask = new TaskUpsertRequest(
       "Съесть буррито", "Скушать буррито очень быстро.",
       TaskStatus.TODO.toString(), USER_PEDRO_ID, USER_JUAN_ID, Set.of(USER_JULIO_ID));
    final var expectedData = new TaskResponse(
        TASK_SECOND_ID, "Съесть буррито", "Скушать буррито очень быстро.",
        Instant.now(), Instant.now(), TaskStatus.TODO, pedro, juan, Set.of(julio));

    this.webTestClient.put().uri("/api/v1/tasks/{id}", TASK_SECOND_ID)
        .body(Mono.just(requestedTask), TaskUpsertRequest.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(TaskResponse.class)
        .value(taskResponse -> {
          Assertions.assertEquals(expectedData.id(), taskResponse.id());
          Assertions.assertNotNull(taskResponse.createdAt());
          Assertions.assertNotNull(taskResponse.updatedAt());
          Assertions.assertEquals(expectedData.name(), taskResponse.name());
          Assertions.assertEquals(expectedData.description(), taskResponse.description());
          Assertions.assertEquals(expectedData.status(), taskResponse.status());
          Assertions.assertEquals(expectedData.author(), taskResponse.author());
          Assertions.assertEquals(expectedData.assignee(), taskResponse.assignee());
          Assertions.assertEquals(expectedData.observers(), taskResponse.observers());
        });

    StepVerifier.create(taskRepository.count())
        .expectNext(2L)
        .expectComplete()
        .verify();
  }

  @Test
  void whenDeleteTaskById_thenDeleteTaskFromDb() {
    this.webTestClient.delete().uri("/api/v1/tasks/{id}", TASK_SECOND_ID)
        .exchange()
        .expectStatus().isNoContent();

    StepVerifier.create(this.taskRepository.count())
        .expectNext(1L)
        .expectComplete()
        .verify();
  }
}
