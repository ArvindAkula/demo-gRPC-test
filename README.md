# Todo Application with gRPC and React

A full-stack Todo application with a React frontend and gRPC Java backend, demonstrating the integration of gRPC with a modern web application.

## Project Structure

- `backend/`: Java Spring Boot backend with gRPC implementation
  - Service implementation for Todo CRUD operations
  - REST controller as a bridge between the frontend and gRPC services
  - JUnit5 tests for all components
- `frontend/`: React frontend
  - Component-based UI for adding, editing, and deleting todos
  - Integration with backend using Axios
- `proto/`: Protocol Buffers definitions

## Features

- Add new todo items
- Edit existing todo items
- Delete todo items
- Mark todo items as completed
- Persistent storage with H2 database

## Technical Overview

This project demonstrates the following architectural approach:

1. Backend services are implemented using gRPC
2. A REST controller acts as an adapter between the frontend and gRPC services
3. The frontend communicates with the backend using REST APIs
4. Protocol Buffers define the service contracts

This architecture provides:
- Type safety with Protocol Buffers
- Efficient client-server communication with gRPC
- Familiar REST API for frontend integration

## Setup and Running

### Backend

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Build the project:
   ```bash
   ./mvnw clean install
   ```

3. Run the server:
   ```bash
   ./mvnw spring-boot:run
   ```

The backend will start on port 8080 with the gRPC server running on port 9090.

### Frontend

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

The frontend will start on port 3000 and automatically proxy API requests to the backend.

## Running Tests

### Backend Tests

```bash
cd backend
./mvnw test
```

### Frontend Tests

```bash
cd frontend
npm test
```

## Technologies Used

- **Frontend**:
  - React
  - Axios
  - CSS with component-scoped styling

- **Backend**:
  - Java 11
  - Spring Boot
  - gRPC
  - JUnit 5 for testing
  - H2 Database (embedded)

- **Integration**:
  - Protocol Buffers (proto3)
  - REST API adapter pattern
