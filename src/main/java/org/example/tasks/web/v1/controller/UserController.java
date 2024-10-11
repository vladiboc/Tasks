package org.example.tasks.web.v1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.text.MessageFormat;
import javax.net.ssl.SSLEngineResult;
import lombok.RequiredArgsConstructor;
import org.example.tasks.aop.Loggable;
import org.example.tasks.constant.ErrorMsg;
import org.example.tasks.mapper.v1.UserMapper;
import org.example.tasks.service.UserService;
import org.example.tasks.web.v1.dto.UserResponse;
import org.example.tasks.web.v1.dto.UserUpsertRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Контроллер для сервиса пользователей.
 */
@Loggable
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Сервис пользователей", description = "CRUD сервис управления пользователями.")
public class UserController {
  private final UserService userService;
  private final UserMapper userMapper;

  @GetMapping
  public Flux<UserResponse> getAllUsers() {
    return this.userService.findAll()
        .map(this.userMapper::userToResponse);
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<UserResponse>> getById(@PathVariable final String id) {
    final var monoUser = this.userService.findById(id);
    return monoUser
        .map(this.userMapper::userToResponse)
        .map(ResponseEntity::ok);
  }

  @PostMapping
  public Mono<ResponseEntity<UserResponse>> create(@RequestBody final UserUpsertRequest request) {
    return this.userService.create(this.userMapper.requstToUser(request))
        .map(this.userMapper::userToResponse)
        .map(ResponseEntity::ok);
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<UserResponse>> update(
      @PathVariable final String id, @RequestBody final UserUpsertRequest request) {
    return this.userService.update(id, this.userMapper.requstToUser(request))
        .map(this.userMapper::userToResponse)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> deleteById(@PathVariable final String id) {
    return this.userService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
  }
}