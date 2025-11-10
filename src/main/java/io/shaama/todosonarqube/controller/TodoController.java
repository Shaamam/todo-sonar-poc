package io.shaama.todosonarqube.controller;

import io.shaama.todosonarqube.dto.TodoRequest;
import io.shaama.todosonarqube.dto.TodoResponse;
import io.shaama.todosonarqube.model.Todo;
import io.shaama.todosonarqube.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getAllTodos() {
        List<Todo> todos = todoService.getAllTodos();
        List<TodoResponse> responses = todos.stream()
                .map(TodoResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getTodoById(@PathVariable Long id) {
        Todo todo = todoService.getTodoById(id);
        return ResponseEntity.ok(TodoResponse.from(todo));
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@RequestBody TodoRequest request) {
        Todo todo = todoService.createTodo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(TodoResponse.from(todo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(
            @PathVariable Long id,
            @RequestBody TodoRequest request) {
        Todo todo = todoService.updateTodo(id, request);
        return ResponseEntity.ok(TodoResponse.from(todo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{completed}")
    public ResponseEntity<List<TodoResponse>> getTodosByStatus(@PathVariable boolean completed) {
        List<Todo> todos = todoService.getTodosByStatus(completed);
        List<TodoResponse> responses = todos.stream()
                .map(TodoResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoResponse> toggleTodoStatus(@PathVariable Long id) {
        Todo todo = todoService.toggleTodoStatus(id);
        return ResponseEntity.ok(TodoResponse.from(todo));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTodoCount() {
        long count = todoService.getTodoCount();
        return ResponseEntity.ok(count);
    }
}
