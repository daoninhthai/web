<div align="center">

# EngWeb - English Learning Web Platform

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

A full-stack English learning web platform with a Spring Boot RESTful backend and a vanilla JavaScript frontend, designed for interactive language education.

[Getting Started](#getting-started) | [Architecture](#architecture) | [API Reference](#api-reference) | [Tech Stack](#tech-stack)

</div>

---

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      Client Browser                          │
│                  (HTML / CSS / JavaScript)                    │
└──────────────────────────┬──────────────────────────────────┘
                           │
            ┌──────────────┴──────────────┐
            ▼                             ▼
┌──────────────────────┐    ┌──────────────────────────────┐
│  fe-english/ (:3000) │    │  be-english/ (:8080)         │
│  Vanilla JS Frontend │    │  Spring Boot Backend         │
│                      │    │                              │
│  ┌────────────────┐  │    │  ┌─────────────────────────┐ │
│  │  public/       │  │    │  │  Controller Layer       │ │
│  │  - index.html  │  │    │  │  - LessonController     │ │
│  │  - assets      │  │    │  │  - QuizController       │ │
│  └────────────────┘  │    │  │  - UserController       │ │
│  ┌────────────────┐  │    │  └──────────┬──────────────┘ │
│  │  src/          │  │    │             │                 │
│  │  - app.js      │──┼────│─▶ ┌────────┴──────────────┐ │
│  │  - api.js      │  │REST│  │  Service Layer         │ │
│  │  - views/      │  │API │  │  - Business Logic      │ │
│  │  - styles/     │  │    │  │  - Validation           │ │
│  └────────────────┘  │    │  └────────┬──────────────┘ │
└──────────────────────┘    │           │                 │
                            │  ┌────────┴──────────────┐ │
                            │  │  Repository Layer     │ │
                            │  │  - JPA Repositories   │ │
                            │  │  - Custom Queries     │ │
                            │  └────────┬──────────────┘ │
                            └───────────┼────────────────┘
                                        │  JDBC
                                        ▼
                           ┌─────────────────────┐
                           │   MySQL (:3306)      │
                           │  english_learning    │
                           │                     │
                           │  Tables:            │
                           │  - lessons          │
                           │  - quizzes          │
                           │  - users            │
                           │  - progress         │
                           └─────────────────────┘
```

## Tech Stack

| Layer        | Technology     | Version | Purpose                          |
|:-------------|:---------------|:--------|:---------------------------------|
| **Frontend** | HTML5/CSS3/JS  | ES6+    | User interface                   |
| **Backend**  | Spring Boot    | 2.x+    | RESTful API server               |
| **Language** | Java           | 17      | Backend language                 |
| **Database** | MySQL          | 8.0     | Relational data storage          |
| **ORM**      | Spring Data JPA| -       | Database abstraction layer       |
| **Build**    | Maven          | 3.9+    | Dependency management            |
| **Server**   | Nginx          | Alpine  | Static file serving (production) |
| **Container**| Docker         | 20+     | Containerization                 |
| **CI/CD**    | GitHub Actions | -       | Automated testing pipeline       |

## Features

- **English Learning Modules** - Structured lessons for language learning
- **Interactive Quizzes** - Test knowledge with quizzes and exercises
- **Progress Tracking** - Track learning progress over time
- **RESTful API** - Clean Spring Boot backend with JPA
- **Responsive Frontend** - Mobile-friendly vanilla JS interface
- **Docker Support** - Full containerization with Docker Compose
- **CI/CD Pipeline** - Automated build and test with GitHub Actions

## Project Structure

```
EngWeb/
├── be-english/                        # Backend Module
│   └── EnglishLearningWeb/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/
│       │   │   │   └── com/.../
│       │   │   │       ├── controller/    # REST controllers
│       │   │   │       ├── model/         # Entity classes
│       │   │   │       ├── repository/    # JPA repositories
│       │   │   │       ├── service/       # Business logic
│       │   │   │       └── config/        # App configuration
│       │   │   └── resources/
│       │   │       └── application.properties
│       │   └── test/                      # Unit tests
│       ├── pom.xml                        # Maven config
│       ├── mvnw                           # Maven wrapper
│       └── LICENSE
├── fe-english/                        # Frontend Module
│   └── FE/
│       ├── public/                    # Static assets
│       │   ├── index.html             # Entry point
│       │   └── assets/                # Images, fonts
│       ├── src/                       # Source code
│       │   ├── app.js                 # Main application
│       │   ├── api.js                 # API client
│       │   ├── views/                 # Page views
│       │   └── styles/                # CSS styles
│       ├── package.json
│       └── yarn.lock
├── .github/workflows/ci.yml          # CI/CD pipeline
├── Dockerfile                         # Multi-stage Docker build
├── docker-compose.yml                 # Container orchestration
└── .gitignore
```

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+ (or use included Maven wrapper)
- Node.js 18+ and npm
- MySQL 8.0+ (or Docker)

### Quick Start with Docker

```bash
# Clone the repository
git clone https://github.com/your-username/EngWeb.git
cd EngWeb

# Start all services
docker compose up -d

# Access the application
# Frontend: http://localhost:3000
# Backend:  http://localhost:8080
# Database: localhost:3306
```

### Local Development

#### 1. Set up the Database

```bash
# Start MySQL (if not running)
mysql -u root -p

# Create database
CREATE DATABASE english_learning;
CREATE USER 'enguser'@'localhost' IDENTIFIED BY 'engpass';
GRANT ALL PRIVILEGES ON english_learning.* TO 'enguser'@'localhost';
FLUSH PRIVILEGES;
```

#### 2. Start the Backend

```bash
cd be-english/EnglishLearningWeb

# Make Maven wrapper executable
chmod +x mvnw

# Build and run
./mvnw clean install
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

#### 3. Start the Frontend

```bash
cd fe-english/FE

# Install dependencies
npm install

# Start development server (or open index.html directly)
npx serve public
```

The frontend will be available at `http://localhost:3000`

### Environment Variables

| Variable            | Description                | Default            |
|:--------------------|:---------------------------|:-------------------|
| `DB_NAME`           | Database name              | `english_learning` |
| `DB_USER`           | Database username          | `enguser`          |
| `DB_PASSWORD`       | Database password          | `engpass`          |
| `DB_ROOT_PASSWORD`  | MySQL root password        | `rootpass`         |
| `DB_PORT`           | Database port              | `3306`             |
| `BACKEND_PORT`      | Backend server port        | `8080`             |
| `FRONTEND_PORT`     | Frontend server port       | `3000`             |

## API Reference

### Lessons

| Method   | Endpoint              | Description              |
|:---------|:----------------------|:-------------------------|
| `GET`    | `/api/lessons`        | List all lessons         |
| `GET`    | `/api/lessons/{id}`   | Get lesson by ID         |
| `POST`   | `/api/lessons`        | Create a new lesson      |
| `PUT`    | `/api/lessons/{id}`   | Update a lesson          |
| `DELETE` | `/api/lessons/{id}`   | Delete a lesson          |

### Quizzes

| Method   | Endpoint              | Description              |
|:---------|:----------------------|:-------------------------|
| `GET`    | `/api/quizzes`        | List all quizzes         |
| `GET`    | `/api/quizzes/{id}`   | Get quiz by ID           |
| `POST`   | `/api/quizzes`        | Create a new quiz        |
| `POST`   | `/api/quizzes/{id}/submit` | Submit quiz answers |

### Users

| Method   | Endpoint              | Description              |
|:---------|:----------------------|:-------------------------|
| `POST`   | `/api/users/register` | Register new user        |
| `POST`   | `/api/users/login`    | User login               |
| `GET`    | `/api/users/progress` | Get learning progress    |

### Example Request

```bash
# Get all lessons
curl http://localhost:8080/api/lessons

# Create a new lesson
curl -X POST http://localhost:8080/api/lessons \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Basic Greetings",
    "content": "Learn common English greetings",
    "level": "beginner"
  }'
```

## Docker Deployment

### Services Overview

| Service      | Port | Purpose                    |
|:-------------|:-----|:---------------------------|
| **backend**  | 8080 | Spring Boot API server     |
| **frontend** | 3000 | Nginx static file server   |
| **database** | 3306 | MySQL database             |

### Docker Commands

```bash
# Start all services
docker compose up -d

# View logs
docker compose logs -f backend

# Stop all services
docker compose down

# Rebuild after code changes
docker compose up -d --build

# Reset database
docker compose down -v
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/new-lesson`)
3. Commit your changes (`git commit -m 'Add new lesson module'`)
4. Push to the branch (`git push origin feature/new-lesson`)
5. Open a Pull Request

## License

This project is available for educational and development purposes.

---

<div align="center">

**Built with Spring Boot and JavaScript**

</div>
