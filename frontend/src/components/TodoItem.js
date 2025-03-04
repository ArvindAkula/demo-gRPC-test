import React from 'react';
import './TodoItem.css';

const TodoItem = ({ todo, deleteTodo, editTodo, updateTodo }) => {
  const handleToggleComplete = () => {
    const updatedTodo = {
      ...todo,
      completed: !todo.completed
    };
    updateTodo(updatedTodo);
  };

  return (
    <div className={`todo-item ${todo.completed ? 'completed' : ''}`}>
      <div className="todo-content">
        <div className="todo-checkbox">
          <input 
            type="checkbox" 
            checked={todo.completed} 
            onChange={handleToggleComplete}
          />
        </div>
        <div className="todo-details">
          <h3 className="todo-title">{todo.title}</h3>
          <p className="todo-description">{todo.description}</p>
        </div>
      </div>
      <div className="todo-actions">
        <button className="edit-btn" onClick={() => editTodo(todo)}>Edit</button>
        <button className="delete-btn" onClick={() => deleteTodo(todo.id)}>Delete</button>
      </div>
    </div>
  );
};

export default TodoItem;
