package org.example.tasks.web.v1.controller;

import java.util.List;
import org.example.tasks.AbstractTest;
import org.example.tasks.web.v1.dto.UserResponse;
import org.example.tasks.web.v1.dto.UserUpsertRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class UserControllerTest extends AbstractTest {
  @Test
  void whenGetAllUsers_thenReturnAllUsersFromDB() {
    final var juan = new UserResponse(USER_JUAN_ID, "Хуан", "juan@mail.mx");
    final var pedro = new UserResponse(USER_PEDRO_ID, "Педро", "pedro@mail.mx");
    final var julio = new UserResponse(USER_JULIO_ID, "Хулио", "julio@mail.mx");
    final var expectedData = List.of(juan, pedro, julio);

    this.webTestClient.get().uri("/api/v1/users")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(UserResponse.class)
        .hasSize(3)
        .contains(expectedData.toArray(UserResponse[]::new));
  }

  @Test
  void whenGetUserById_thenReturnUserById() {
    final var expectedData = new UserResponse(USER_JUAN_ID, "Хуан", "juan@mail.mx");

    this.webTestClient.get().uri("/api/v1/users/{id}", USER_JUAN_ID)
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserResponse.class)
        .isEqualTo(expectedData);
  }

  @Test
  void whenCreateUser_thenReturnNewUser() {
    StepVerifier.create(userRepository.count())
        .expectNext(3L)
        .expectComplete()
        .verify();
    final var requestedUser = new UserUpsertRequest("Педро", "pedro@mail.mx");
    final var expectedData = new UserResponse(USER_NEW_ID, "Педро", "pedro@mail.mx");
    this.webTestClient.post().uri("/api/v1/users")
        .body(Mono.just(requestedUser), UserUpsertRequest.class)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(UserResponse.class)
        .value(userResponse -> {
          Assertions.assertNotNull(userResponse.id());
          Assertions.assertEquals(expectedData.name(), userResponse.name());
          Assertions.assertEquals(expectedData.email(), userResponse.email());
        });
    StepVerifier.create(userRepository.count())
        .expectNext(4L)
        .expectComplete()
        .verify();
  }

  @Test
  void whenUpdateUser_thenReturnUpdatedUser() {
    final var requestedUser = new UserUpsertRequest("Хьюго", "hugo@mail.mx");
    final var expectedData = new UserResponse(USER_JULIO_ID, "Хьюго", "hugo@mail.mx");
    this.webTestClient.put().uri("/api/v1/users/{id}", USER_JULIO_ID)
        .body(Mono.just(requestedUser), UserUpsertRequest.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserResponse.class)
        .value(userResponse -> {
          Assertions.assertEquals(expectedData, userResponse);
        });
    StepVerifier.create(this.userRepository.findById(USER_JULIO_ID))
        .expectNextCount(1L)
        .verifyComplete();
  }

  @Test
  void whenDeleteUserById_thenDeleteUserFromDB() {
    this.webTestClient.delete().uri("/api/v1/users/{id}", USER_PEDRO_ID)
        .exchange()
        .expectStatus().isNoContent();
    StepVerifier.create(this.userRepository.count())
        .expectNext(2L)
        .expectComplete()
        .verify();
  }
}
