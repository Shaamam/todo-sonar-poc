package io.shaama.todosonarqube.dto;

import io.shaama.todosonarqube.model.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TodoResponseTest {

    @Test
    @DisplayName("Should create TodoResponse from Todo")
    void testFromTodo() {
        // Given
        Todo todo = Todo.builder()
                .id(1L)
                .title("Test Todo")
                .description("Test Description")
                .completed(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        TodoResponse response = TodoResponse.from(todo);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(todo.getId());
        assertThat(response.getTitle()).isEqualTo(todo.getTitle());
        assertThat(response.getDescription()).isEqualTo(todo.getDescription());
        assertThat(response.isCompleted()).isEqualTo(todo.isCompleted());
        assertThat(response.getCreatedAt()).isEqualTo(todo.getCreatedAt());
        assertThat(response.getUpdatedAt()).isEqualTo(todo.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create TodoResponse using builder")
    void testBuilder() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        TodoResponse response = TodoResponse.builder()
                .id(1L)
                .title("Test Todo")
                .description("Test Description")
                .completed(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test Todo");
        assertThat(response.getDescription()).isEqualTo("Test Description");
        assertThat(response.isCompleted()).isFalse();
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getUpdatedAt()).isEqualTo(now);
    }
}
