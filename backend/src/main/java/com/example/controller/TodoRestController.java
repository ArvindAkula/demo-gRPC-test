package com.example.controller;

import com.example.entity.Todo;
import com.example.grpc.todo.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TodoRestController {

    private final TodoServiceGrpc.TodoServiceBlockingStub todoServiceStub;

    public TodoRestController() {
        // Create a gRPC channel to connect to the gRPC server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        
        // Create a blocking stub for making synchronous calls to the gRPC service
        todoServiceStub = TodoServiceGrpc.newBlockingStub(channel);
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        GetAllTodosRequest request = GetAllTodosRequest.newBuilder().build();
        GetAllTodosResponse response = todoServiceStub.getAllTodos(request);
        
        List<Todo> todos = response.getTodosList().stream()
                .map(this::mapToEntityTodo)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        GetTodoRequest request = GetTodoRequest.newBuilder()
                .setId(id)
                .build();
        
        try {
            com.example.grpc.todo.Todo grpcTodo = todoServiceStub.getTodo(request);
            Todo todo = mapToEntityTodo(grpcTodo);
            return ResponseEntity.ok(todo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        CreateTodoRequest request = CreateTodoRequest.newBuilder()
                .setTitle(todo.getTitle())
                .setDescription(todo.getDescription())
                .setCompleted(todo.isCompleted())
                .build();
        
        com.example.grpc.todo.Todo grpcTodo = todoServiceStub.createTodo(request);
        Todo createdTodo = mapToEntityTodo(grpcTodo);
        
        return ResponseEntity.ok(createdTodo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        // Ensure the path ID matches the todo ID
        if (!id.equals(todo.getId())) {
            return ResponseEntity.badRequest().build();
        }
        
        UpdateTodoRequest request = UpdateTodoRequest.newBuilder()
                .setId(todo.getId())
                .setTitle(todo.getTitle())
                .setDescription(todo.getDescription())
                .setCompleted(todo.isCompleted())
                .build();
        
        try {
            com.example.grpc.todo.Todo grpcTodo = todoServiceStub.updateTodo(request);
            Todo updatedTodo = mapToEntityTodo(grpcTodo);
            return ResponseEntity.ok(updatedTodo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteTodo(@PathVariable Long id) {
        DeleteTodoRequest request = DeleteTodoRequest.newBuilder()
                .setId(id)
                .build();
        
        DeleteTodoResponse response = todoServiceStub.deleteTodo(request);
        
        return ResponseEntity.ok(Map.of("success", response.getSuccess()));
    }
    
    private Todo mapToEntityTodo(com.example.grpc.todo.Todo grpcTodo) {
        return Todo.builder()
                .id(grpcTodo.getId())
                .title(grpcTodo.getTitle())
                .description(grpcTodo.getDescription())
                .completed(grpcTodo.getCompleted())
                .build();
    }
}
