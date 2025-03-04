# Todo Application with gRPC

A simple Todo application with a React frontend and gRPC Java backend.

## Project Structure

- `backend/`: Java backend with gRPC implementation
- `frontend/`: React frontend
- `proto/`: Protocol Buffers definitions

## Setup and Running

### Backend

1. Navigate to the backend directory
2. Run `./mvnw clean install` to build the project
3. Run `./mvnw spring-boot:run` to start the server

### Frontend

1. Navigate to the frontend directory
2. Run `npm install` to install dependencies
3. Run `npm start` to start the development server

## Features

- Add new todo items
- Edit existing todo items
- Delete todo items
- Mark todo items as completed

## Technologies Used

- Frontend: React, Axios, gRPC-Web
- Backend: Java, Spring Boot, gRPC
- Database: H2 (embedded)