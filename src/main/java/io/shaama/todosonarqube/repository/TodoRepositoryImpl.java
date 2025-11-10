package io.shaama.todosonarqube.repository;

import io.shaama.todosonarqube.model.Todo;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class TodoRepositoryImpl implements TodoRepository {
    
    private final Map<Long, Todo> todoStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public TodoRepositoryImpl() {
        // Initialize with some mock data
        initializeMockData();
    }

    private void initializeMockData() {
        Todo todo1 = Todo.builder()
                .id(idGenerator.incrementAndGet())
                .title("Learn Spring Boot")
                .description("Complete Spring Boot tutorial")
                .completed(false)
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now().minusDays(2))
                .build();

        Todo todo2 = Todo.builder()
                .id(idGenerator.incrementAndGet())
                .title("Setup SonarQube")
                .description("Configure SonarQube for code coverage")
                .completed(true)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();

        Todo todo3 = Todo.builder()
                .id(idGenerator.incrementAndGet())
                .title("Write Unit Tests")
                .description("Achieve 80% code coverage")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        todoStore.put(todo1.getId(), todo1);
        todoStore.put(todo2.getId(), todo2);
        todoStore.put(todo3.getId(), todo3);
    }

    @Override
    public List<Todo> findAll() {
        return new ArrayList<>(todoStore.values());
    }

    @Override
    public Optional<Todo> findById(Long id) {
        return Optional.ofNullable(todoStore.get(id));
    }

    @Override
    public Todo save(Todo todo) {
        if (todo.getId() == null) {
            // New todo
            Long newId = idGenerator.incrementAndGet();
            todo.setId(newId);
            todo.setCreatedAt(LocalDateTime.now());
            todo.setUpdatedAt(LocalDateTime.now());
        } else {
            // Update existing todo
            todo.setUpdatedAt(LocalDateTime.now());
        }
        todoStore.put(todo.getId(), todo);
        return todo;
    }

    @Override
    public void deleteById(Long id) {
        todoStore.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return todoStore.containsKey(id);
    }

    @Override
    public List<Todo> findByCompleted(boolean completed) {
        return todoStore.values().stream()
                .filter(todo -> todo.isCompleted() == completed)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return todoStore.size();
    }
}
