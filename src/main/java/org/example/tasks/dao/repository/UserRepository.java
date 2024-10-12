package org.example.tasks.dao.repository;

import org.example.tasks.dao.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Асинхронный Mongo-репозиторий для сущности User.
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
