package com.example.repository;

import com.example.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TodoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testFindAll() {
        // given
        Todo todo1 = Todo.builder()
                .title("Test Todo 1")
                .description("Description 1")
                .completed(false)
                .build();

        Todo todo2 = Todo.builder()
                .title("Test Todo 2")
                .description("Description 2")
                .completed(true)
                .build();

        entityManager.persist(todo1);
        entityManager.persist(todo2);
        entityManager.flush();

        // when
        List<Todo> todos = todoRepository.findAll();

        // then
        assertThat(todos).hasSize(2);
        assertThat(todos).extracting(Todo::getTitle).containsExactlyInAnyOrder("Test Todo 1", "Test Todo 2");
    }

    @Test
    public void testFindById() {
        // given
        Todo todo = Todo.builder()
                .title("Test Todo")
                .description("Description")
                .completed(false)
                .build();

        entityManager.persist(todo);
        entityManager.flush();

        // when
        Optional<Todo> found = todoRepository.findById(todo.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test Todo");
        assertThat(found.get().getDescription()).isEqualTo("Description");
        assertThat(found.get().isCompleted()).isFalse();
    }

    @Test
    public void testSave() {
        // given
        Todo todo = Todo.builder()
                .title("New Todo")
                .description("New Description")
                .completed(false)
                .build();

        // when
        Todo savedTodo = todoRepository.save(todo);

        // then
        assertThat(savedTodo.getId()).isNotNull();
        assertThat(savedTodo.getTitle()).isEqualTo("New Todo");
        assertThat(savedTodo.getDescription()).isEqualTo("New Description");
        assertThat(savedTodo.isCompleted()).isFalse();
    }

    @Test
    public void testDelete() {
        // given
        Todo todo = Todo.builder()
                .title("Todo to Delete")
                .description("Will be deleted")
                .completed(false)
                .build();

        entityManager.persist(todo);
        entityManager.flush();
        
        // when
        todoRepository.deleteById(todo.getId());
        
        // then
        Optional<Todo> deleted = todoRepository.findById(todo.getId());
        assertThat(deleted).isEmpty();
    }
}
