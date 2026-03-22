# Todo Application with SonarQube Integration

A Spring Boot Todo application with comprehensive unit and integration tests for SonarQube code coverage analysis.

## Features

- **RESTful API** for managing todos
- **In-memory repository** with mock data (no database required)
- **Comprehensive test coverage** for services, controllers, and repositories
- **JaCoCo integration** for code coverage reports
- **SonarQube ready** for code quality analysis





## Project Structure

```
src/
├── main/
│   └── java/io/shaama/todosonarqube/
│       ├── controller/
│       │   └── TodoController.java          # REST endpoints
│       ├── dto/
│       │   ├── TodoRequest.java             # Request DTO
│       │   └── TodoResponse.java            # Response DTO
│       ├── exception/
│       │   ├── ErrorResponse.java           # Error response model
│       │   ├── GlobalExceptionHandler.java  # Global exception handler
│       │   ├── InvalidTodoException.java    # Custom exception
│       │   └── TodoNotFoundException.java   # Custom exception
│       ├── model/
│       │   └── Todo.java                    # Todo entity
│       ├── repository/
│       │   ├── TodoRepository.java          # Repository interface
│       │   └── TodoRepositoryImpl.java      # In-memory implementation
│       ├── service/
│       │   ├── TodoService.java             # Service interface
│       │   └── TodoServiceImpl.java         # Service implementation
│       └── TodoSonarqubeApplication.java    # Main application
└── test/
    └── java/io/shaama/todosonarqube/
        ├── controller/
        │   └── TodoControllerTest.java      # Controller tests
        ├── dto/
        │   └── TodoResponseTest.java        # DTO tests
        ├── exception/
        │   ├── GlobalExceptionHandlerTest.java
        │   ├── InvalidTodoExceptionTest.java
        │   └── TodoNotFoundExceptionTest.java
        ├── repository/
        │   └── TodoRepositoryImplTest.java  # Repository tests
        └── service/
            └── TodoServiceImplTest.java     # Service tests
```

## API Endpoints

### Get All Todos
```bash
GET /api/todos
```

### Get Todo by ID
```bash
GET /api/todos/{id}
```

### Create Todo
```bash
POST /api/todos
Content-Type: application/json

{
  "title": "Learn Spring Boot",
  "description": "Complete the tutorial",
  "completed": false
}
```

### Update Todo
```bash
PUT /api/todos/{id}
Content-Type: application/json

{
  "title": "Updated Title",
  "description": "Updated Description",
  "completed": true
}
```

### Delete Todo
```bash
DELETE /api/todos/{id}
```

### Get Todos by Status
```bash
GET /api/todos/status/{completed}
# Example: GET /api/todos/status/true
```

### Toggle Todo Status
```bash
PATCH /api/todos/{id}/toggle
```

### Get Todo Count
```bash
GET /api/todos/count
```

## Running the Application

### Build the Application
```bash
./gradlew build
```

### Run the Application
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### Run Tests
```bash
./gradlew test
```

### Generate Code Coverage Report
```bash
./gradlew test jacocoTestReport
```

The coverage report will be available at:
- HTML: `build/reports/jacoco/test/html/index.html`
- XML: `build/reports/jacoco/test/jacocoTestReport.xml`

## SonarQube Analysis

### Prerequisites
1. Install and run SonarQube locally (default: http://localhost:9000)
2. Generate a SonarQube token from the SonarQube dashboard

### Run SonarQube Analysis
```bash
./gradlew sonar -Dsonar.login=YOUR_SONAR_TOKEN
```

Or with all parameters:
```bash
./gradlew sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=YOUR_SONAR_TOKEN \
  -Dsonar.projectKey=todo-sonarqube \
  -Dsonar.projectName="Todo SonarQube"
```

### View Results
After running the analysis, view the results at:
`http://localhost:9000/dashboard?id=todo-sonarqube`

## Test Coverage

The project includes comprehensive tests covering:

### Service Layer (`TodoServiceImpl`)
- ✅ Get all todos
- ✅ Get todo by ID (success and not found)
- ✅ Create todo with validation
- ✅ Update todo with validation
- ✅ Delete todo (success and not found)
- ✅ Get todos by status
- ✅ Toggle todo status
- ✅ Get todo count
- ✅ Title validation (null, empty, too long)
- ✅ Description validation (too long)

### Controller Layer (`TodoController`)
- ✅ All REST endpoints
- ✅ Success scenarios
- ✅ Error scenarios (404, 400)
- ✅ JSON serialization/deserialization

### Repository Layer (`TodoRepositoryImpl`)
- ✅ CRUD operations
- ✅ Mock data initialization
- ✅ Find by status
- ✅ Count operations

### Exception Handling
- ✅ Global exception handler
- ✅ Custom exceptions
- ✅ Error response formatting

## Technologies Used

- **Java 21**
- **Spring Boot 3.5.7**
- **Lombok** - Reduce boilerplate code
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework
- **AssertJ** - Fluent assertions
- **JaCoCo** - Code coverage
- **SonarQube** - Code quality analysis

## Mock Data

The application initializes with 3 mock todos:
1. "Learn Spring Boot" - incomplete
2. "Setup SonarQube" - completed
3. "Write Unit Tests" - incomplete

## Notes

- No database required - all data is stored in-memory
- Data is reset on application restart
- Thread-safe repository implementation using `ConcurrentHashMap`
- Comprehensive validation for todos
- Global exception handling with proper HTTP status codes
