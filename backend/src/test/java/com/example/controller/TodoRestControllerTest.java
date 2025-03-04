package com.example.controller;

import com.example.entity.Todo;
import com.example.grpc.todo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TodoRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TodoServiceGrpc.TodoServiceBlockingStub todoServiceStub;

    @InjectMocks
    private TodoRestController todoRestController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(todoRestController).build();
    }

    @Test
    void testGetAllTodos() throws Exception {
        // Mock gRPC response
        com.example.grpc.todo.Todo grpcTodo1 = com.example.grpc.todo.Todo.newBuilder()
                .setId(1L)
                .setTitle("Test Todo 1")
                .setDescription("Description 1")
                .setCompleted(false)
                .build();
        
        com.example.grpc.todo.Todo grpcTodo2 = com.example.grpc.todo.Todo.newBuilder()
                .setId(2L)
                .setTitle("Test Todo 2")
                .setDescription("Description 2")
                .setCompleted(true)
                .build();
        
        GetAllTodosResponse grpcResponse = GetAllTodosResponse.newBuilder()
                .addTodos(grpcTodo1)
                .addTodos(grpcTodo2)
                .build();
        
        when(todoServiceStub.getAllTodos(any(GetAllTodosRequest.class))).thenReturn(grpcResponse);

        mockMvc.perform(get("/api/todos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Todo 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].completed").value(true));
    }

    @Test
    void testGetTodoById() throws Exception {
        // Mock gRPC response
        com.example.grpc.todo.Todo grpcTodo = com.example.grpc.todo.Todo.newBuilder()
                .setId(1L)
                .setTitle("Test Todo")
                .setDescription("Description")
                .setCompleted(false)
                .build();
        
        when(todoServiceStub.getTodo(any(GetTodoRequest.class))).thenReturn(grpcTodo);

        mockMvc.perform(get("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void testCreateTodo() throws Exception {
        // Prepare request
        Todo todo = Todo.builder()
                .title("New Todo")
                .description("New Description")
                .completed(false)
                .build();
        
        // Mock gRPC response
        com.example.grpc.todo.Todo grpcTodo = com.example.grpc.todo.Todo.newBuilder()
                .setId(1L)
                .setTitle("New Todo")
                .setDescription("New Description")
                .setCompleted(false)
                .build();
        
        when(todoServiceStub.createTodo(any(CreateTodoRequest.class))).thenReturn(grpcTodo);

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Todo"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void testUpdateTodo() throws Exception {
        // Prepare request
        Todo todo = Todo.builder()
                .id(1L)
                .title("Updated Todo")
                .description("Updated Description")
                .completed(true)
                .build();
        
        // Mock gRPC response
        com.example.grpc.todo.Todo grpcTodo = com.example.grpc.todo.Todo.newBuilder()
                .setId(1L)
                .setTitle("Updated Todo")
                .setDescription("Updated Description")
                .setCompleted(true)
                .build();
        
        when(todoServiceStub.updateTodo(any(UpdateTodoRequest.class))).thenReturn(grpcTodo);

        mockMvc.perform(put("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Todo"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void testDeleteTodo() throws Exception {
        // Mock gRPC response
        DeleteTodoResponse grpcResponse = DeleteTodoResponse.newBuilder()
                .setSuccess(true)
                .build();
        
        when(todoServiceStub.deleteTodo(any(DeleteTodoRequest.class))).thenReturn(grpcResponse);

        mockMvc.perform(delete("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
