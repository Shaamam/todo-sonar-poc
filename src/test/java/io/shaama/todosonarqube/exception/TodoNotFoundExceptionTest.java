package io.shaama.todosonarqube.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TodoNotFoundExceptionTest {

    @Test
    @DisplayName("Should create exception with custom message")
    void testTodoNotFoundException_WithMessage() {
        // Given
        String message = "Custom error message";

        // When
        TodoNotFoundException exception = new TodoNotFoundException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should create exception with id")
    void testTodoNotFoundException_WithId() {
        // Given
        Long id = 123L;

        // When
        TodoNotFoundException exception = new TodoNotFoundException(id);

        // Then
        assertThat(exception.getMessage()).contains("123");
        assertThat(exception.getMessage()).contains("not found");
    }
}
