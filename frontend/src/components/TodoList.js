import React from 'react';
import TodoItem from './TodoItem';
import './TodoList.css';

const TodoList = ({ todos, deleteTodo, editTodo, updateTodo }) => {
  if (!todos.length) {
    return <div className="empty-list">No todos yet. Add a new one to get started!</div>;
  }

  return (
    <div className="todo-list">
      <h2>Your Todos</h2>
      <div className="todo-items">
        {todos.map(todo => (
          <TodoItem 
            key={todo.id} 
            todo={todo} 
            deleteTodo={deleteTodo} 
            editTodo={editTodo}
            updateTodo={updateTodo}
          />
        ))}
      </div>
    </div>
  );
};

export default TodoList;
