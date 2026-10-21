
# Canteen API

A REST API for a canteen ordering system built with Spring Boot and PostgreSQL.

## Features

- User registration and login with JWT authentication
- Role-based access control for users, staff, and admins
- Menu browsing by category
- Menu item management
- Order creation and tracking
- Order status management for staff

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT authentication
- PostgreSQL
- Docker

## Getting Started

### Prerequisites

- Java 21
- Docker and Docker Compose

### Setup

1. Clone the repository.

2. Create a `.env` file in the project root with the required database and JWT configuration.

3. Start the application and database:

   ```bash
   docker compose up --build
   ```

The API will be available at `http://localhost:8080`.

## API

The API includes endpoints for authentication, menu browsing, order management, and staff operations.

Staff and admin endpoints require authentication with the appropriate role.

API requests can be tested using the Bruno collection included in the repository.

## Database

PostgreSQL is used for data persistence. Hibernate manages schema updates during development.

## License

This project is for learning purposes.
```