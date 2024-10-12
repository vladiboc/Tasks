package org.example.tasks.dao.repository;

import org.example.tasks.dao.entity.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Асинхронный Mongo-репозиторий для сущности Task.
 */
@Repository
public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
}
