import React, { useState, useEffect } from 'react';
import './App.css';
import TodoList from './components/TodoList';
import TodoForm from './components/TodoForm';
import TodoService from './services/TodoService';

function App() {
  const [todos, setTodos] = useState([]);
  const [currentTodo, setCurrentTodo] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchTodos();
  }, []);

  const fetchTodos = async () => {
    try {
      setLoading(true);
      const fetchedTodos = await TodoService.getAllTodos();
      setTodos(fetchedTodos);
      setError(null);
    } catch (err) {
      console.error('Error fetching todos:', err);
      setError('Failed to load todos. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  const addTodo = async (todo) => {
    try {
      const newTodo = await TodoService.createTodo(todo);
      setTodos([...todos, newTodo]);
    } catch (err) {
      console.error('Error adding todo:', err);
      setError('Failed to add todo. Please try again.');
    }
  };

  const updateTodo = async (todo) => {
    try {
      const updatedTodo = await TodoService.updateTodo(todo);
      setTodos(todos.map(t => t.id === updatedTodo.id ? updatedTodo : t));
      setCurrentTodo(null);
    } catch (err) {
      console.error('Error updating todo:', err);
      setError('Failed to update todo. Please try again.');
    }
  };

  const deleteTodo = async (id) => {
    try {
      const success = await TodoService.deleteTodo(id);
      if (success) {
        setTodos(todos.filter(todo => todo.id !== id));
      }
    } catch (err) {
      console.error('Error deleting todo:', err);
      setError('Failed to delete todo. Please try again.');
    }
  };

  const editTodo = (todo) => {
    setCurrentTodo(todo);
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Todo Application</h1>
      </header>
      <main>
        {error && <div className="error-message">{error}</div>}
        
        <TodoForm 
          addTodo={addTodo} 
          updateTodo={updateTodo} 
          currentTodo={currentTodo} 
          setCurrentTodo={setCurrentTodo} 
        />
        
        {loading ? (
          <div className="loading">Loading todos...</div>
        ) : (
          <TodoList 
            todos={todos} 
            deleteTodo={deleteTodo} 
            editTodo={editTodo}
            updateTodo={updateTodo}
          />
        )}
      </main>
    </div>
  );
}

export default App;
