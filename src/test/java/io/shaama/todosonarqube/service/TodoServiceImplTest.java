package io.shaama.todosonarqube.service;

import io.shaama.todosonarqube.dto.TodoRequest;
import io.shaama.todosonarqube.exception.InvalidTodoException;
import io.shaama.todosonarqube.exception.TodoNotFoundException;
import io.shaama.todosonarqube.model.Todo;
import io.shaama.todosonarqube.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    private Todo testTodo;
    private TodoRequest testRequest;

    @BeforeEach
    void setUp() {
        testTodo = Todo.builder()
                .id(1L)
                .title("Test Todo")
                .description("Test Description")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testRequest = TodoRequest.builder()
                .title("Test Todo")
                .description("Test Description")
                .completed(false)
                .build();
    }

    @Test
    @DisplayName("Should get all todos successfully")
    void testGetAllTodos() {
        // Given
        List<Todo> todos = Arrays.asList(testTodo);
        when(todoRepository.findAll()).thenReturn(todos);

        // When
        List<Todo> result = todoService.getAllTodos();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testTodo);
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get todo by id successfully")
    void testGetTodoById_Success() {
        // Given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(testTodo));

        // When
        Todo result = todoService.getTodoById(1L);

        // Then
        assertThat(result).isEqualTo(testTodo);
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw TodoNotFoundException when todo not found")
    void testGetTodoById_NotFound() {
        // Given
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> todoService.getTodoById(999L))
                .isInstanceOf(TodoNotFoundException.class)
                .hasMessageContaining("999");
        verify(todoRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should create todo successfully")
    void testCreateTodo_Success() {
        // Given
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        // When
        Todo result = todoService.createTodo(testRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(testRequest.getTitle());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should create todo with default completed status when not provided")
    void testCreateTodo_DefaultCompleted() {
        // Given
        TodoRequest requestWithoutCompleted = TodoRequest.builder()
                .title("New Todo")
                .description("New Description")
                .build();
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        // When
        Todo result = todoService.createTodo(requestWithoutCompleted);

        // Then
        assertThat(result).isNotNull();
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should throw InvalidTodoException when title is null")
    void testCreateTodo_NullTitle() {
        // Given
        TodoRequest invalidRequest = TodoRequest.builder()
                .title(null)
                .description("Description")
                .build();

        // When & Then
        assertThatThrownBy(() -> todoService.createTodo(invalidRequest))
                .isInstanceOf(InvalidTodoException.class)
                .hasMessageContaining("title cannot be empty");
        verify(todoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidTodoException when title is empty")
    void testCreateTodo_EmptyTitle() {
        // Given
        TodoRequest invalidRequest = TodoRequest.builder()
                .title("   ")
                .description("Description")
                .build();

        // When & Then
        assertThatThrownBy(() -> todoService.createTodo(invalidRequest))
                .isInstanceOf(InvalidTodoException.class)
                .hasMessageContaining("title cannot be empty");
        verify(todoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidTodoException when title exceeds 200 characters")
    void testCreateTodo_TitleTooLong() {
        // Given
        String longTitle = "a".repeat(201);
        TodoRequest invalidRequest = TodoRequest.builder()
                .title(longTitle)
                .description("Description")
                .build();

        // When & Then
        assertThatThrownBy(() -> todoService.createTodo(invalidRequest))
                .isInstanceOf(InvalidTodoException.class)
                .hasMessageContaining("title cannot exceed 200 characters");
        verify(todoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidTodoException when description exceeds 1000 characters")
    void testCreateTodo_DescriptionTooLong() {
        // Given
        String longDescription = "a".repeat(1001);
        TodoRequest invalidRequest = TodoRequest.builder()
                .title("Valid Title")
                .description(longDescription)
                .build();

        // When & Then
        assertThatThrownBy(() -> todoService.createTodo(invalidRequest))
                .isInstanceOf(InvalidTodoException.class)
                .hasMessageContaining("description cannot exceed 1000 characters");
        verify(todoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update todo successfully")
    void testUpdateTodo_Success() {
        // Given
        TodoRequest updateRequest = TodoRequest.builder()
                .title("Updated Title")
                .description("Updated Description")
                .completed(true)
                .build();
        when(todoRepository.findById(1L)).thenReturn(Optional.of(testTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        // When
        Todo result = todoService.updateTodo(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should update todo without changing completed status when not provided")
    void testUpdateTodo_WithoutCompletedStatus() {
        // Given
        TodoRequest updateRequest = TodoRequest.builder()
                .title("Updated Title")
                .description("Updated Description")
                .build();
        when(todoRepository.findById(1L)).thenReturn(Optional.of(testTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        // When
        Todo result = todoService.updateTodo(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should throw TodoNotFoundException when updating non-existent todo")
    void testUpdateTodo_NotFound() {
        // Given
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> todoService.updateTodo(999L, testRequest))
                .isInstanceOf(TodoNotFoundException.class);
        verify(todoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete todo successfully")
    void testDeleteTodo_Success() {
        // Given
        when(todoRepository.existsById(1L)).thenReturn(true);

        // When
        todoService.deleteTodo(1L);

        // Then
        verify(todoRepository, times(1)).existsById(1L);
        verify(todoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw TodoNotFoundException when deleting non-existent todo")
    void testDeleteTodo_NotFound() {
        // Given
        when(todoRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> todoService.deleteTodo(999L))
                .isInstanceOf(TodoNotFoundException.class);
        verify(todoRepository, times(1)).existsById(999L);
        verify(todoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should get todos by completed status")
    void testGetTodosByStatus() {
        // Given
        List<Todo> completedTodos = Arrays.asList(testTodo);
        when(todoRepository.findByCompleted(true)).thenReturn(completedTodos);

        // When
        List<Todo> result = todoService.getTodosByStatus(true);

        // Then
        assertThat(result).hasSize(1);
        verify(todoRepository, times(1)).findByCompleted(true);
    }

    @Test
    @DisplayName("Should toggle todo status successfully")
    void testToggleTodoStatus_Success() {
        // Given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(testTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        // When
        Todo result = todoService.toggleTodoStatus(1L);

        // Then
        assertThat(result).isNotNull();
        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should throw TodoNotFoundException when toggling non-existent todo")
    void testToggleTodoStatus_NotFound() {
        // Given
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> todoService.toggleTodoStatus(999L))
                .isInstanceOf(TodoNotFoundException.class);
        verify(todoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get todo count successfully")
    void testGetTodoCount() {
        // Given
        when(todoRepository.count()).thenReturn(5L);

        // When
        long result = todoService.getTodoCount();

        // Then
        assertThat(result).isEqualTo(5L);
        verify(todoRepository, times(1)).count();
    }
}
