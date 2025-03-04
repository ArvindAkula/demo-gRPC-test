package com.example.service;

import com.example.entity.Todo;
import com.example.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    private TodoService todoService;

    @BeforeEach
    void setUp() {
        todoService = new TodoService(todoRepository);
    }

    @Test
    void testGetAllTodos() {
        // given
        Todo todo1 = Todo.builder().id(1L).title("Todo 1").description("Description 1").completed(false).build();
        Todo todo2 = Todo.builder().id(2L).title("Todo 2").description("Description 2").completed(true).build();

        when(todoRepository.findAll()).thenReturn(Arrays.asList(todo1, todo2));

        // when
        List<Todo> todos = todoService.getAllTodos();

        // then
        assertThat(todos).hasSize(2);
        assertThat(todos).extracting(Todo::getTitle).containsExactly("Todo 1", "Todo 2");
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void testGetTodoById() {
        // given
        Todo todo = Todo.builder().id(1L).title("Todo 1").description("Description 1").completed(false).build();
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        // when
        Optional<Todo> found = todoService.getTodoById(1L);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Todo 1");
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTodoById_NotFound() {
        // given
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        Optional<Todo> found = todoService.getTodoById(1L);

        // then
        assertThat(found).isEmpty();
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateTodo() {
        // given
        Todo todoToCreate = Todo.builder().title("New Todo").description("New Description").completed(false).build();
        Todo savedTodo = Todo.builder().id(1L).title("New Todo").description("New Description").completed(false).build();

        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        // when
        Todo created = todoService.createTodo(todoToCreate);

        // then
        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getTitle()).isEqualTo("New Todo");
        verify(todoRepository, times(1)).save(todoToCreate);
    }

    @Test
    void testUpdateTodo() {
        // given
        Todo todoToUpdate = Todo.builder().id(1L).title("Updated Todo").description("Updated Description").completed(true).build();
        when(todoRepository.save(any(Todo.class))).thenReturn(todoToUpdate);

        // when
        Todo updated = todoService.updateTodo(todoToUpdate);

        // then
        assertThat(updated.getTitle()).isEqualTo("Updated Todo");
        assertThat(updated.isCompleted()).isTrue();
        verify(todoRepository, times(1)).save(todoToUpdate);
    }

    @Test
    void testDeleteTodo_Success() {
        // given
        when(todoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(todoRepository).deleteById(1L);

        // when
        boolean result = todoService.deleteTodo(1L);

        // then
        assertThat(result).isTrue();
        verify(todoRepository, times(1)).existsById(1L);
        verify(todoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTodo_NotFound() {
        // given
        when(todoRepository.existsById(1L)).thenReturn(false);

        // when
        boolean result = todoService.deleteTodo(1L);

        // then
        assertThat(result).isFalse();
        verify(todoRepository, times(1)).existsById(1L);
        verify(todoRepository, never()).deleteById(1L);
    }
}
