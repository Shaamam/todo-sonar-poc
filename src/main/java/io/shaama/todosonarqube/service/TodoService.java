package io.shaama.todosonarqube.service;

import io.shaama.todosonarqube.dto.TodoRequest;
import io.shaama.todosonarqube.model.Todo;

import java.util.List;

public interface TodoService {
    List<Todo> getAllTodos();
    Todo getTodoById(Long id);
    Todo createTodo(TodoRequest request);
    Todo updateTodo(Long id, TodoRequest request);
    void deleteTodo(Long id);
    List<Todo> getTodosByStatus(boolean completed);
    Todo toggleTodoStatus(Long id);
    long getTodoCount();
}
