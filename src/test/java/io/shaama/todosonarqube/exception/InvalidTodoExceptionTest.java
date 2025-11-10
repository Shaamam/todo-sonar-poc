package io.shaama.todosonarqube.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidTodoExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void testInvalidTodoException_WithMessage() {
        // Given
        String message = "Invalid todo data";

        // When
        InvalidTodoException exception = new InvalidTodoException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}
