package org.example.tasks.service;

import org.example.tasks.dao.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Контракт сервиса пользователей.
 */
public interface UserService {
  Flux<User> findAll();

  Mono<User> findById(String id);

  Flux<User> findAllById(Iterable<String> ids);

  Mono<User> create(User user);

  Mono<User> update(String id, User user);

  Mono<Void> deleteById(String id);
}
