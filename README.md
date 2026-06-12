# team-dependency-risk-analyzer

A production-quality full-stack web application that identifies project risks caused by knowledge concentration and employee dependency.

## Tech Stack

* Backend: Java 21, Spring Boot 3.2.5, Spring Security, JWT
* Database: MySQL 8.x, Spring Data JPA, Hibernate
* Frontend: React.js, Chart.js, Axios
* Testing: JUnit 5, Mockito
* Tools: Maven, Git, Postman

## Core Features

* JWT Authentication with Role-Based Access Control
* Employee and Skill Management
* Project and Module Management
* Risk Calculation Engine (custom scoring algorithm)
* Skill Gap Analysis
* Employee Exit Simulation
* Risk Dashboard with Charts
* Unit Testing with 7 passing tests

## How to Run

### Backend

1. Create MySQL database: team_risk_analyzer_db
2. Update application.properties with your MySQL password
3. Run TeamDependencyRiskAnalyzerApplication.java

### Frontend

1. cd frontend
2. npm install
3. npm start

## Login Credentials

* Admin: [admin@riskanalyzer.com](mailto:admin@riskanalyzer.com) / password123
* Manager: [manager@riskanalyzer.com](mailto:manager@riskanalyzer.com) / password123

## Architecture

Frontend (React) → REST APIs → Service Layer → Repository → MySQL
