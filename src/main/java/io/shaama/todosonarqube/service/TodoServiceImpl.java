package io.shaama.todosonarqube.service;

import io.shaama.todosonarqube.dto.TodoRequest;
import io.shaama.todosonarqube.exception.InvalidTodoException;
import io.shaama.todosonarqube.exception.TodoNotFoundException;
import io.shaama.todosonarqube.model.Todo;
import io.shaama.todosonarqube.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Override
    public List<Todo> getAllTodos() {
        log.info("Fetching all todos");
        return todoRepository.findAll();
    }

    @Override
    public Todo getTodoById(Long id) {
        log.info("Fetching todo with id: {}", id);
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    @Override
    public Todo createTodo(TodoRequest request) {
        log.info("Creating new todo with title: {}", request.getTitle());
        validateTodoRequest(request);
        
        Todo todo = Todo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(request.getCompleted() != null ? request.getCompleted() : false)
                .build();
        
        Todo savedTodo = todoRepository.save(todo);
        log.info("Todo created with id: {}", savedTodo.getId());
        return savedTodo;
    }

    @Override
    public Todo updateTodo(Long id, TodoRequest request) {
        log.info("Updating todo with id: {}", id);
        validateTodoRequest(request);
        
        Todo existingTodo = getTodoById(id);
        
        existingTodo.setTitle(request.getTitle());
        existingTodo.setDescription(request.getDescription());
        if (request.getCompleted() != null) {
            existingTodo.setCompleted(request.getCompleted());
        }
        
        Todo updatedTodo = todoRepository.save(existingTodo);
        log.info("Todo updated with id: {}", updatedTodo.getId());
        return updatedTodo;
    }

    @Override
    public void deleteTodo(Long id) {
        log.info("Deleting todo with id: {}", id);
        if (!todoRepository.existsById(id)) {
            throw new TodoNotFoundException(id);
        }
        todoRepository.deleteById(id);
        log.info("Todo deleted with id: {}", id);
    }

    @Override
    public List<Todo> getTodosByStatus(boolean completed) {
        log.info("Fetching todos with completed status: {}", completed);
        return todoRepository.findByCompleted(completed);
    }

    @Override
    public Todo toggleTodoStatus(Long id) {
        log.info("Toggling status for todo with id: {}", id);
        Todo todo = getTodoById(id);
        todo.setCompleted(!todo.isCompleted());
        Todo updatedTodo = todoRepository.save(todo);
        log.info("Todo status toggled for id: {}, new status: {}", id, updatedTodo.isCompleted());
        return updatedTodo;
    }

    @Override
    public long getTodoCount() {
        log.info("Fetching total todo count");
        return todoRepository.count();
    }

    private void validateTodoRequest(TodoRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new InvalidTodoException("Todo title cannot be empty");
        }
        if (request.getTitle().length() > 200) {
            throw new InvalidTodoException("Todo title cannot exceed 200 characters");
        }
        if (request.getDescription() != null && request.getDescription().length() > 1000) {
            throw new InvalidTodoException("Todo description cannot exceed 1000 characters");
        }
    }
}
