Notes Management API
A robust Spring Boot RESTful API for managing personal notes with MongoDB integration.
This application allows users to create, read, update, and delete notes with advanced features like tagging, text statistics, and pagination.

Features

Complete CRUD Operations - Create, read, update, and delete notes

Smart Tagging System - Categorize notes with BUSINESS, PERSONAL, or IMPORTANT tags

Text Analytics - Automatic word frequency statistics for each note

Advanced Filtering - Filter notes by tags with pagination support

RESTful Design - Clean API endpoints following REST conventions

Comprehensive Validation - Input validation and meaningful error messages

Containerized - Ready for Docker deployment with MongoDB

Technologies Used

Java 17 - Modern Java programming language

Spring Boot 3.4.0 - Rapid application development framework

Spring Data MongoDB - MongoDB integration and repository abstraction

MongoDB - NoSQL document database

Docker & Docker Compose - Containerization and orchestration

Maven - Dependency management and build automation

JUnit 5 & Mockito - Comprehensive testing framework

Lombok - Boilerplate code reduction

Validation API - Input validation and constraints

API Endpoints
Notes Management
Method	Endpoint	Description
GET	/api/notes	Get all notes (with pagination and tag filtering)
GET	/api/notes/{id}	Get note details with statistics
GET	/api/notes/{id}/stats	Get only note statistics
POST	/api/notes	Create a new note
PUT	/api/notes/{id}	Update an existing note
DELETE	/api/notes/{id}	Delete a note

Quick Start
Prerequisites
Java 17 or higher

Maven 3.6+

Docker and Docker Compose

Running with Docker (Recommended)
Clone and build the project:

bash
git clone <repository-url>
cd notesapp
Build and run with Docker Compose:

bash
docker-compose up --build
Access the application:

text
http://localhost:8080

Running Locally
Start MongoDB:

bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
Build and run the application:

bash
mvn clean package
java -jar target/notes-application-1.0.0.jar
