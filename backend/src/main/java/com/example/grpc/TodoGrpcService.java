package com.example.grpc;

import com.example.entity.Todo;
import com.example.grpc.todo.*;
import com.example.service.TodoService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GrpcService
public class TodoGrpcService extends TodoServiceGrpc.TodoServiceImplBase {

    private final TodoService todoService;

    @Autowired
    public TodoGrpcService(TodoService todoService) {
        this.todoService = todoService;
    }

    @Override
    public void getAllTodos(GetAllTodosRequest request, StreamObserver<GetAllTodosResponse> responseObserver) {
        List<Todo> todos = todoService.getAllTodos();
        
        List<com.example.grpc.todo.Todo> grpcTodos = todos.stream()
                .map(this::mapToGrpcTodo)
                .collect(Collectors.toList());
        
        GetAllTodosResponse response = GetAllTodosResponse.newBuilder()
                .addAllTodos(grpcTodos)
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTodo(GetTodoRequest request, StreamObserver<com.example.grpc.todo.Todo> responseObserver) {
        Long id = request.getId();
        Optional<Todo> todoOptional = todoService.getTodoById(id);
        
        if (todoOptional.isPresent()) {
            com.example.grpc.todo.Todo grpcTodo = mapToGrpcTodo(todoOptional.get());
            responseObserver.onNext(grpcTodo);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Todo not found with id " + id)
                    .asRuntimeException());
        }
    }

    @Override
    public void createTodo(CreateTodoRequest request, StreamObserver<com.example.grpc.todo.Todo> responseObserver) {
        Todo todo = Todo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(request.getCompleted())
                .build();
        
        Todo savedTodo = todoService.createTodo(todo);
        com.example.grpc.todo.Todo grpcTodo = mapToGrpcTodo(savedTodo);
        
        responseObserver.onNext(grpcTodo);
        responseObserver.onCompleted();
    }

    @Override
    public void updateTodo(UpdateTodoRequest request, StreamObserver<com.example.grpc.todo.Todo> responseObserver) {
        Long id = request.getId();
        Optional<Todo> todoOptional = todoService.getTodoById(id);
        
        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            todo.setTitle(request.getTitle());
            todo.setDescription(request.getDescription());
            todo.setCompleted(request.getCompleted());
            
            Todo updatedTodo = todoService.updateTodo(todo);
            com.example.grpc.todo.Todo grpcTodo = mapToGrpcTodo(updatedTodo);
            
            responseObserver.onNext(grpcTodo);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Todo not found with id " + id)
                    .asRuntimeException());
        }
    }

    @Override
    public void deleteTodo(DeleteTodoRequest request, StreamObserver<DeleteTodoResponse> responseObserver) {
        Long id = request.getId();
        boolean deleted = todoService.deleteTodo(id);
        
        DeleteTodoResponse response = DeleteTodoResponse.newBuilder()
                .setSuccess(deleted)
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    private com.example.grpc.todo.Todo mapToGrpcTodo(Todo todo) {
        return com.example.grpc.todo.Todo.newBuilder()
                .setId(todo.getId())
                .setTitle(todo.getTitle())
                .setDescription(todo.getDescription())
                .setCompleted(todo.isCompleted())
                .build();
    }
}
