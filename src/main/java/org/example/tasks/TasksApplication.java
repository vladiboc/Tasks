package org.example.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Реактивный бекенд для управления задачами.
 */
@SpringBootApplication
public class TasksApplication {

  public static void main(String[] args) {
    SpringApplication.run(TasksApplication.class, args);
  }
}
