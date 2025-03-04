import axios from 'axios';

// Base URL for API requests
const API_URL = '/api/todos';

class TodoService {
  /**
   * Get all todos
   * @returns {Promise<Array>} list of todos
   */
  static async getAllTodos() {
    try {
      const response = await axios.get(API_URL);
      return response.data;
    } catch (error) {
      console.error('Error fetching todos:', error);
      throw error;
    }
  }

  /**
   * Get a specific todo by ID
   * @param {number} id - Todo ID
   * @returns {Promise<Object>} todo object
   */
  static async getTodoById(id) {
    try {
      const response = await axios.get(`${API_URL}/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching todo with id ${id}:`, error);
      throw error;
    }
  }

  /**
   * Create a new todo
   * @param {Object} todoData - Todo data (title, description, completed)
   * @returns {Promise<Object>} created todo
   */
  static async createTodo(todoData) {
    try {
      const response = await axios.post(API_URL, todoData);
      return response.data;
    } catch (error) {
      console.error('Error creating todo:', error);
      throw error;
    }
  }

  /**
   * Update an existing todo
   * @param {Object} todo - Todo object with id, title, description, completed
   * @returns {Promise<Object>} updated todo
   */
  static async updateTodo(todo) {
    try {
      const response = await axios.put(`${API_URL}/${todo.id}`, todo);
      return response.data;
    } catch (error) {
      console.error(`Error updating todo with id ${todo.id}:`, error);
      throw error;
    }
  }

  /**
   * Delete a todo by ID
   * @param {number} id - Todo ID
   * @returns {Promise<boolean>} success status
   */
  static async deleteTodo(id) {
    try {
      const response = await axios.delete(`${API_URL}/${id}`);
      return response.data.success;
    } catch (error) {
      console.error(`Error deleting todo with id ${id}:`, error);
      throw error;
    }
  }
}

export default TodoService;
