package io.shaama.todosonarqube.repository;

import io.shaama.todosonarqube.model.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    List<Todo> findAll();
    Optional<Todo> findById(Long id);
    Todo save(Todo todo);
    void deleteById(Long id);
    boolean existsById(Long id);
    List<Todo> findByCompleted(boolean completed);
    long count();
}
