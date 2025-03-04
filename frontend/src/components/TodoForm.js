import React, { useState, useEffect } from 'react';
import './TodoForm.css';

const TodoForm = ({ addTodo, updateTodo, currentTodo, setCurrentTodo }) => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [completed, setCompleted] = useState(false);

  // Reset form when currentTodo changes
  useEffect(() => {
    if (currentTodo) {
      setTitle(currentTodo.title);
      setDescription(currentTodo.description);
      setCompleted(currentTodo.completed);
    } else {
      resetForm();
    }
  }, [currentTodo]);

  const resetForm = () => {
    setTitle('');
    setDescription('');
    setCompleted(false);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (!title.trim()) {
      alert('Title is required!');
      return;
    }
    
    if (currentTodo) {
      // Update existing todo
      updateTodo({
        ...currentTodo,
        title,
        description,
        completed
      });
    } else {
      // Add new todo
      addTodo({
        title,
        description,
        completed
      });
    }
    
    resetForm();
  };

  const cancelEdit = () => {
    setCurrentTodo(null);
    resetForm();
  };

  return (
    <form className="todo-form" onSubmit={handleSubmit}>
      <h2>{currentTodo ? 'Edit Todo' : 'Add New Todo'}</h2>
      
      <div className="form-group">
        <label htmlFor="title">Title</label>
        <input
          type="text"
          id="title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="Enter todo title"
          required
        />
      </div>
      
      <div className="form-group">
        <label htmlFor="description">Description</label>
        <textarea
          id="description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Enter todo description"
          rows="3"
        />
      </div>
      
      <div className="form-check">
        <input
          type="checkbox"
          id="completed"
          checked={completed}
          onChange={(e) => setCompleted(e.target.checked)}
        />
        <label htmlFor="completed">Completed</label>
      </div>
      
      <div className="form-actions">
        <button type="submit" className="btn-submit">
          {currentTodo ? 'Update' : 'Add'}
        </button>
        
        {currentTodo && (
          <button 
            type="button" 
            className="btn-cancel" 
            onClick={cancelEdit}
          >
            Cancel
          </button>
        )}
      </div>
    </form>
  );
};

export default TodoForm;
