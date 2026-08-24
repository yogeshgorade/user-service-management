# User Management Service

User Management REST API built using Spring Boot and PostgreSQL.

This project is developed as a real-world backend application to gain practical experience with REST API development, validation, exception handling, pagination, sorting, searching, logging, testing, API documentation, Git/GitHub and cloud deployment.

## 🚀 Project Objective

The main objective of this project is to build a backend application similar to a production application rather than just a basic CRUD project.

The application provides APIs for:

- Creating users
- Retrieving users
- Updating users
- Deleting users
- Searching users
- Pagination
- Sorting
- Request validation
- Global exception handling
- API documentation using Swagger/OpenAPI
- Application health monitoring using Spring Boot Actuator
- Application logging
- Unit and controller testing

The backend is also designed to be consumed by a frontend application such as Angular.

---

## 🛠️ Tech Stack

| Technology | Usage |
|---|---|
| Java 21 | Programming language |
| Spring Boot | Backend framework |
| Spring Web MVC | REST APIs |
| Spring Data JPA | Database interaction |
| Hibernate | ORM |
| PostgreSQL | Database |
| Maven | Build and dependency management |
| Lombok | Boilerplate reduction |
| Bean Validation | Request validation |
| Swagger / OpenAPI | API documentation |
| Spring Boot Actuator | Application monitoring |
| Logback | Logging |
| JUnit 5 | Testing |
| Mockito | Unit testing |
| Git | Version control |
| GitHub | Source code management |

---

## 🏗️ Application Architecture

The application follows a layered architecture:

Controller
↓
Service
↓
Repository
↓
PostgreSQL Database

### Layers

### Controller Layer

Responsible for:

- Receiving HTTP requests
- Validating request payloads
- Returning HTTP responses
- Exposing REST endpoints

### Service Layer

Responsible for:

- Business logic
- Duplicate email/mobile validation
- User existence validation
- Pagination
- Sorting
- Searching
- Entity/DTO conversion

### Repository Layer

Responsible for:

- Database operations
- Custom search queries
- Pagination and sorting through Spring Data JPA

### DTO Layer

The application uses DTOs to avoid directly exposing database entities through API responses.

Main DTOs:

- CreateUserRequest
- UpdateUserRequest
- UserResponse

### Entity Layer

The `User` entity represents the user table in PostgreSQL.

---

# 🔐 Entity vs DTO

Database entities are not directly exposed from the REST API.

The application uses:

```text
Request
   ↓
CreateUserRequest / UpdateUserRequest
   ↓
Service
   ↓
User Entity
   ↓
Database