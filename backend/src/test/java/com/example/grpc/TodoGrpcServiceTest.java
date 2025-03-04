package com.example.grpc;

import com.example.entity.Todo;
import com.example.grpc.todo.*;
import com.example.service.TodoService;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoGrpcServiceTest {

    @Mock
    private TodoService todoService;

    private TodoGrpcService todoGrpcService;

    @BeforeEach
    void setUp() {
        todoGrpcService = new TodoGrpcService(todoService);
    }

    @Test
    void testGetAllTodos() throws Exception {
        // given
        Todo todo1 = Todo.builder().id(1L).title("Todo 1").description("Description 1").completed(false).build();
        Todo todo2 = Todo.builder().id(2L).title("Todo 2").description("Description 2").completed(true).build();
        List<Todo> todos = Arrays.asList(todo1, todo2);

        when(todoService.getAllTodos()).thenReturn(todos);

        GetAllTodosRequest request = GetAllTodosRequest.newBuilder().build();
        StreamRecorder<GetAllTodosResponse> responseObserver = StreamRecorder.create();

        // when
        todoGrpcService.getAllTodos(request, responseObserver);

        // then
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("The call did not terminate in time");
        }

        assertThat(responseObserver.getError()).isNull();
        List<GetAllTodosResponse> results = responseObserver.getValues();
        assertThat(results).hasSize(1);
        GetAllTodosResponse response = results.get(0);
        assertThat(response.getTodosList()).hasSize(2);
        assertThat(response.getTodosList().get(0).getTitle()).isEqualTo("Todo 1");
        assertThat(response.getTodosList().get(1).getTitle()).isEqualTo("Todo 2");

        verify(todoService, times(1)).getAllTodos();
    }

    @Test
    void testGetTodo() throws Exception {
        // given
        Todo todo = Todo.builder().id(1L).title("Todo 1").description("Description 1").completed(false).build();
        when(todoService.getTodoById(1L)).thenReturn(Optional.of(todo));

        GetTodoRequest request = GetTodoRequest.newBuilder().setId(1L).build();
        StreamRecorder<com.example.grpc.todo.Todo> responseObserver = StreamRecorder.create();

        // when
        todoGrpcService.getTodo(request, responseObserver);

        // then
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("The call did not terminate in time");
        }

        assertThat(responseObserver.getError()).isNull();
        List<com.example.grpc.todo.Todo> results = responseObserver.getValues();
        assertThat(results).hasSize(1);
        com.example.grpc.todo.Todo response = results.get(0);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Todo 1");

        verify(todoService, times(1)).getTodoById(1L);
    }

    @Test
    void testCreateTodo() throws Exception {
        // given
        Todo todo = Todo.builder().id(1L).title("New Todo").description("New Description").completed(false).build();
        when(todoService.createTodo(any(Todo.class))).thenReturn(todo);

        CreateTodoRequest request = CreateTodoRequest.newBuilder()
                .setTitle("New Todo")
                .setDescription("New Description")
                .setCompleted(false)
                .build();

        StreamRecorder<com.example.grpc.todo.Todo> responseObserver = StreamRecorder.create();

        // when
        todoGrpcService.createTodo(request, responseObserver);

        // then
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("The call did not terminate in time");
        }

        assertThat(responseObserver.getError()).isNull();
        List<com.example.grpc.todo.Todo> results = responseObserver.getValues();
        assertThat(results).hasSize(1);
        com.example.grpc.todo.Todo response = results.get(0);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("New Todo");

        verify(todoService, times(1)).createTodo(any(Todo.class));
    }

    @Test
    void testUpdateTodo() throws Exception {
        // given
        Todo existingTodo = Todo.builder().id(1L).title("Todo 1").description("Description 1").completed(false).build();
        Todo updatedTodo = Todo.builder().id(1L).title("Updated Todo").description("Updated Description").completed(true).build();

        when(todoService.getTodoById(1L)).thenReturn(Optional.of(existingTodo));
        when(todoService.updateTodo(any(Todo.class))).thenReturn(updatedTodo);

        UpdateTodoRequest request = UpdateTodoRequest.newBuilder()
                .setId(1L)
                .setTitle("Updated Todo")
                .setDescription("Updated Description")
                .setCompleted(true)
                .build();

        StreamRecorder<com.example.grpc.todo.Todo> responseObserver = StreamRecorder.create();

        // when
        todoGrpcService.updateTodo(request, responseObserver);

        // then
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("The call did not terminate in time");
        }

        assertThat(responseObserver.getError()).isNull();
        List<com.example.grpc.todo.Todo> results = responseObserver.getValues();
        assertThat(results).hasSize(1);
        com.example.grpc.todo.Todo response = results.get(0);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Updated Todo");
        assertThat(response.getCompleted()).isTrue();

        verify(todoService, times(1)).getTodoById(1L);
        verify(todoService, times(1)).updateTodo(any(Todo.class));
    }

    @Test
    void testDeleteTodo() throws Exception {
        // given
        when(todoService.deleteTodo(1L)).thenReturn(true);

        DeleteTodoRequest request = DeleteTodoRequest.newBuilder()
                .setId(1L)
                .build();

        StreamRecorder<DeleteTodoResponse> responseObserver = StreamRecorder.create();

        // when
        todoGrpcService.deleteTodo(request, responseObserver);

        // then
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("The call did not terminate in time");
        }

        assertThat(responseObserver.getError()).isNull();
        List<DeleteTodoResponse> results = responseObserver.getValues();
        assertThat(results).hasSize(1);
        DeleteTodoResponse response = results.get(0);
        assertThat(response.getSuccess()).isTrue();

        verify(todoService, times(1)).deleteTodo(1L);
    }
}
