package org.example.tasks;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.example.tasks.dao.entity.Task;
import org.example.tasks.dao.entity.TaskStatus;
import org.example.tasks.dao.entity.User;
import org.example.tasks.dao.repository.TaskRepository;
import org.example.tasks.dao.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@AutoConfigureWebTestClient
public abstract class AbstractTest {
  protected static final String USER_JUAN_ID = UUID.randomUUID().toString();
  protected static final String USER_PEDRO_ID = UUID.randomUUID().toString();
  protected static final String USER_JULIO_ID = UUID.randomUUID().toString();
  protected static final String USER_NEW_ID = UUID.randomUUID().toString();
  protected static final String TASK_FIRST_ID = UUID.randomUUID().toString();
  protected static final String TASK_SECOND_ID = UUID.randomUUID().toString();
  protected static final Instant CREATED_AT = Instant.now();
  protected static final Instant UPDATED_AT = Instant.now();

  @Container
  static final MongoDBContainer MONGODB_CONTAINER = new MongoDBContainer("mongo:6.0.8")
      .withReuse(true);

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", MONGODB_CONTAINER::getReplicaSetUrl);
  }

  @Autowired
  protected WebTestClient webTestClient;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected TaskRepository taskRepository;

  @BeforeEach
  protected void setup() {
    final var juan = new User(USER_JUAN_ID, "Хуан", "juan@mail.mx");
    final var pedro = new User(USER_PEDRO_ID, "Педро", "pedro@mail.mx");
    final var julio = new User(USER_JULIO_ID, "Хулио", "julio@mail.mx");
    this.userRepository.saveAll(List.of(juan, pedro, julio)).collectList().block();
    this.taskRepository.saveAll(List.of(
        new Task(TASK_FIRST_ID, "Приготовить буррито", "Приготовить буррито с соусом сальса",
            CREATED_AT, UPDATED_AT, TaskStatus.TODO,
            USER_JUAN_ID, USER_PEDRO_ID, Set.of(USER_JULIO_ID),
            juan, pedro, Set.of(julio)
        ),
        new Task(TASK_SECOND_ID, "Съесть буррито", "Скушать полностью буррито с соусом сальса",
            CREATED_AT, UPDATED_AT, TaskStatus.TODO,
            USER_PEDRO_ID, USER_JUAN_ID, Set.of(USER_JULIO_ID),
            pedro, juan, Set.of(julio)
        )
    )).collectList().block();
  }

  @AfterEach
  protected void cleanup() {
    this.userRepository.deleteAll().block();
    this.taskRepository.deleteAll().block();
  }
}
