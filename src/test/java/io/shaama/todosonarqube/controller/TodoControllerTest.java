package io.shaama.todosonarqube.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.shaama.todosonarqube.dto.TodoRequest;
import io.shaama.todosonarqube.exception.InvalidTodoException;
import io.shaama.todosonarqube.exception.TodoNotFoundException;
import io.shaama.todosonarqube.model.Todo;
import io.shaama.todosonarqube.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

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
    @DisplayName("GET /api/todos - Should return all todos")
    void testGetAllTodos() throws Exception {
        // Given
        List<Todo> todos = Arrays.asList(testTodo);
        when(todoService.getAllTodos()).thenReturn(todos);

        // When & Then
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Todo"))
                .andExpect(jsonPath("$[0].description").value("Test Description"))
                .andExpect(jsonPath("$[0].completed").value(false));

        verify(todoService, times(1)).getAllTodos();
    }

    @Test
    @DisplayName("GET /api/todos/{id} - Should return todo by id")
    void testGetTodoById() throws Exception {
        // Given
        when(todoService.getTodoById(1L)).thenReturn(testTodo);

        // When & Then
        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Todo"));

        verify(todoService, times(1)).getTodoById(1L);
    }

    @Test
    @DisplayName("GET /api/todos/{id} - Should return 404 when todo not found")
    void testGetTodoById_NotFound() throws Exception {
        // Given
        when(todoService.getTodoById(999L)).thenThrow(new TodoNotFoundException(999L));

        // When & Then
        mockMvc.perform(get("/api/todos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());

        verify(todoService, times(1)).getTodoById(999L);
    }

    @Test
    @DisplayName("POST /api/todos - Should create new todo")
    void testCreateTodo() throws Exception {
        // Given
        when(todoService.createTodo(any(TodoRequest.class))).thenReturn(testTodo);

        // When & Then
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Todo"));

        verify(todoService, times(1)).createTodo(any(TodoRequest.class));
    }

    @Test
    @DisplayName("POST /api/todos - Should return 400 for invalid todo")
    void testCreateTodo_Invalid() throws Exception {
        // Given
        when(todoService.createTodo(any(TodoRequest.class)))
                .thenThrow(new InvalidTodoException("Todo title cannot be empty"));

        // When & Then
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());

        verify(todoService, times(1)).createTodo(any(TodoRequest.class));
    }

    @Test
    @DisplayName("PUT /api/todos/{id} - Should update todo")
    void testUpdateTodo() throws Exception {
        // Given
        Todo updatedTodo = Todo.builder()
                .id(1L)
                .title("Updated Title")
                .description("Updated Description")
                .completed(true)
                .createdAt(testTodo.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        when(todoService.updateTodo(eq(1L), any(TodoRequest.class))).thenReturn(updatedTodo);

        // When & Then
        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Title"));

        verify(todoService, times(1)).updateTodo(eq(1L), any(TodoRequest.class));
    }

    @Test
    @DisplayName("PUT /api/todos/{id} - Should return 404 when updating non-existent todo")
    void testUpdateTodo_NotFound() throws Exception {
        // Given
        when(todoService.updateTodo(eq(999L), any(TodoRequest.class)))
                .thenThrow(new TodoNotFoundException(999L));

        // When & Then
        mockMvc.perform(put("/api/todos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(todoService, times(1)).updateTodo(eq(999L), any(TodoRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/todos/{id} - Should delete todo")
    void testDeleteTodo() throws Exception {
        // Given
        doNothing().when(todoService).deleteTodo(1L);

        // When & Then
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());

        verify(todoService, times(1)).deleteTodo(1L);
    }

    @Test
    @DisplayName("DELETE /api/todos/{id} - Should return 404 when deleting non-existent todo")
    void testDeleteTodo_NotFound() throws Exception {
        // Given
        doThrow(new TodoNotFoundException(999L)).when(todoService).deleteTodo(999L);

        // When & Then
        mockMvc.perform(delete("/api/todos/999"))
                .andExpect(status().isNotFound());

        verify(todoService, times(1)).deleteTodo(999L);
    }

    @Test
    @DisplayName("GET /api/todos/status/{completed} - Should return todos by status")
    void testGetTodosByStatus() throws Exception {
        // Given
        List<Todo> completedTodos = Arrays.asList(testTodo);
        when(todoService.getTodosByStatus(true)).thenReturn(completedTodos);

        // When & Then
        mockMvc.perform(get("/api/todos/status/true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));

        verify(todoService, times(1)).getTodosByStatus(true);
    }

    @Test
    @DisplayName("PATCH /api/todos/{id}/toggle - Should toggle todo status")
    void testToggleTodoStatus() throws Exception {
        // Given
        Todo toggledTodo = Todo.builder()
                .id(1L)
                .title("Test Todo")
                .description("Test Description")
                .completed(true)
                .createdAt(testTodo.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        when(todoService.toggleTodoStatus(1L)).thenReturn(toggledTodo);

        // When & Then
        mockMvc.perform(patch("/api/todos/1/toggle"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.completed").value(true));

        verify(todoService, times(1)).toggleTodoStatus(1L);
    }

    @Test
    @DisplayName("PATCH /api/todos/{id}/toggle - Should return 404 when toggling non-existent todo")
    void testToggleTodoStatus_NotFound() throws Exception {
        // Given
        when(todoService.toggleTodoStatus(999L)).thenThrow(new TodoNotFoundException(999L));

        // When & Then
        mockMvc.perform(patch("/api/todos/999/toggle"))
                .andExpect(status().isNotFound());

        verify(todoService, times(1)).toggleTodoStatus(999L);
    }

    @Test
    @DisplayName("GET /api/todos/count - Should return todo count")
    void testGetTodoCount() throws Exception {
        // Given
        when(todoService.getTodoCount()).thenReturn(5L);

        // When & Then
        mockMvc.perform(get("/api/todos/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("5"));

        verify(todoService, times(1)).getTodoCount();
    }
}
