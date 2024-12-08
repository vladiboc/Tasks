package org.example.tasks.service;

import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.tasks.aop.Loggable;
import org.example.tasks.dao.entity.User;
import org.example.tasks.dao.repository.UserRepository;
import org.example.tasks.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Реализация сервиса пользователей.
 */
@Loggable
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Flux<User> findAll() {
    return this.userRepository.findAll();
  }

  @Override
  public Mono<User> findById(final String id) {
    return this.userRepository.findById(id)
        .switchIfEmpty(Mono.error(new UserNotFoundException(id)));
  }

  @Override
  public Mono<User> findByName(final String userName) {
    return this.userRepository.findByName(userName)
        .switchIfEmpty(Mono.error(new UserNotFoundException(userName)));
  }

  @Override
  public Flux<User> findAllById(Iterable<String> ids) {
    return this.userRepository.findAllById(ids);
  }

  @Override
  public Mono<User> create(final User user) {
    user.setId(UUID.randomUUID().toString());
    user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    return this.userRepository.save(user);
  }

  @Override
  public Mono<User> update(final String id, final User user) {
    return this.findById(id).flatMap(updatedUser -> {
      if (StringUtils.hasText(user.getName())) {
        updatedUser.setName(user.getName());
      }
      if (StringUtils.hasText(user.getEmail())) {
        updatedUser.setEmail(user.getEmail());
      }
      if (StringUtils.hasText(user.getPassword())) {
        updatedUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
      }
      if (Objects.nonNull(user.getRoles())) {
        updatedUser.setRoles(user.getRoles());
      }
      return this.userRepository.save(updatedUser);
    });
  }

  @Override
  public Mono<Void> deleteById(final String id) {
    return this.userRepository.deleteById(id);
  }
}
