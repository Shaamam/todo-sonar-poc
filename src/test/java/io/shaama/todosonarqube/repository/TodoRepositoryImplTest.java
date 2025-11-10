package io.shaama.todosonarqube.repository;

import io.shaama.todosonarqube.model.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TodoRepositoryImplTest {

    private TodoRepositoryImpl todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository = new TodoRepositoryImpl();
    }

    @Test
    @DisplayName("Should initialize with mock data")
    void testInitialization() {
        // When
        List<Todo> todos = todoRepository.findAll();

        // Then
        assertThat(todos).isNotEmpty();
        assertThat(todos).hasSize(3);
    }

    @Test
    @DisplayName("Should find all todos")
    void testFindAll() {
        // When
        List<Todo> todos = todoRepository.findAll();

        // Then
        assertThat(todos).hasSize(3);
        assertThat(todos).allMatch(todo -> todo.getId() != null);
        assertThat(todos).allMatch(todo -> todo.getTitle() != null);
    }

    @Test
    @DisplayName("Should find todo by id")
    void testFindById_Success() {
        // Given
        List<Todo> allTodos = todoRepository.findAll();
        Long existingId = allTodos.get(0).getId();

        // When
        Optional<Todo> result = todoRepository.findById(existingId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(existingId);
    }

    @Test
    @DisplayName("Should return empty when todo not found by id")
    void testFindById_NotFound() {
        // When
        Optional<Todo> result = todoRepository.findById(999L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should save new todo")
    void testSave_NewTodo() {
        // Given
        Todo newTodo = Todo.builder()
                .title("New Todo")
                .description("New Description")
                .completed(false)
                .build();

        // When
        Todo savedTodo = todoRepository.save(newTodo);

        // Then
        assertThat(savedTodo.getId()).isNotNull();
        assertThat(savedTodo.getCreatedAt()).isNotNull();
        assertThat(savedTodo.getUpdatedAt()).isNotNull();
        assertThat(todoRepository.existsById(savedTodo.getId())).isTrue();
    }

    @Test
    @DisplayName("Should update existing todo")
    void testSave_ExistingTodo() {
        // Given
        List<Todo> allTodos = todoRepository.findAll();
        Todo existingTodo = allTodos.get(0);
        existingTodo.setTitle("Updated Title");

        // When
        Todo updatedTodo = todoRepository.save(existingTodo);

        // Then
        assertThat(updatedTodo.getId()).isEqualTo(existingTodo.getId());
        assertThat(updatedTodo.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedTodo.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should delete todo by id")
    void testDeleteById() {
        // Given
        List<Todo> allTodos = todoRepository.findAll();
        Long idToDelete = allTodos.get(0).getId();

        // When
        todoRepository.deleteById(idToDelete);

        // Then
        assertThat(todoRepository.existsById(idToDelete)).isFalse();
        assertThat(todoRepository.findById(idToDelete)).isEmpty();
    }

    @Test
    @DisplayName("Should check if todo exists by id")
    void testExistsById() {
        // Given
        List<Todo> allTodos = todoRepository.findAll();
        Long existingId = allTodos.get(0).getId();

        // When & Then
        assertThat(todoRepository.existsById(existingId)).isTrue();
        assertThat(todoRepository.existsById(999L)).isFalse();
    }

    @Test
    @DisplayName("Should find todos by completed status - true")
    void testFindByCompleted_True() {
        // When
        List<Todo> completedTodos = todoRepository.findByCompleted(true);

        // Then
        assertThat(completedTodos).isNotEmpty();
        assertThat(completedTodos).allMatch(Todo::isCompleted);
    }

    @Test
    @DisplayName("Should find todos by completed status - false")
    void testFindByCompleted_False() {
        // When
        List<Todo> incompleteTodos = todoRepository.findByCompleted(false);

        // Then
        assertThat(incompleteTodos).isNotEmpty();
        assertThat(incompleteTodos).allMatch(todo -> !todo.isCompleted());
    }

    @Test
    @DisplayName("Should count all todos")
    void testCount() {
        // When
        long count = todoRepository.count();

        // Then
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("Should maintain correct count after adding todo")
    void testCount_AfterAdd() {
        // Given
        long initialCount = todoRepository.count();
        Todo newTodo = Todo.builder()
                .title("New Todo")
                .description("New Description")
                .completed(false)
                .build();

        // When
        todoRepository.save(newTodo);

        // Then
        assertThat(todoRepository.count()).isEqualTo(initialCount + 1);
    }

    @Test
    @DisplayName("Should maintain correct count after deleting todo")
    void testCount_AfterDelete() {
        // Given
        long initialCount = todoRepository.count();
        List<Todo> allTodos = todoRepository.findAll();
        Long idToDelete = allTodos.get(0).getId();

        // When
        todoRepository.deleteById(idToDelete);

        // Then
        assertThat(todoRepository.count()).isEqualTo(initialCount - 1);
    }
}
