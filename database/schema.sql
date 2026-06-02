CREATE DATABASE IF NOT EXISTS team_risk_analyzer_db;
USE team_risk_analyzer_db;

-- Table 1: users
CREATE TABLE users (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    email         VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          ENUM('ADMIN', 'MANAGER') NOT NULL,
    is_active     BOOLEAN DEFAULT TRUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table 2: employees
CREATE TABLE employees (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name    VARCHAR(150) NOT NULL,
    email        VARCHAR(150) NOT NULL UNIQUE,
    department   VARCHAR(100),
    designation  VARCHAR(100),
    joining_date DATE,
    status       ENUM('ACTIVE','ON_LEAVE','RESIGNED') DEFAULT 'ACTIVE',
    user_id      BIGINT UNIQUE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Table 3: skills
CREATE TABLE skills (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    category    ENUM('FRONTEND','BACKEND','DATABASE','CLOUD','DEVOPS','MOBILE','OTHER') NOT NULL,
    criticality ENUM('COMMON','MODERATE','RARE') DEFAULT 'COMMON',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table 4: employee_skills
CREATE TABLE employee_skills (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    skill_id    BIGINT NOT NULL,
    proficiency ENUM('BEGINNER','INTERMEDIATE','EXPERT') NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_emp_skill (employee_id, skill_id),
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id)    REFERENCES skills(id)    ON DELETE CASCADE
);

-- Table 5: projects
CREATE TABLE projects (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    description TEXT,
    start_date  DATE,
    status      ENUM('ACTIVE','ON_HOLD','COMPLETED') DEFAULT 'ACTIVE',
    criticality ENUM('LOW','MEDIUM','HIGH','CRITICAL') DEFAULT 'MEDIUM',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table 6: modules
CREATE TABLE modules (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id  BIGINT NOT NULL,
    name        VARCHAR(200) NOT NULL,
    description TEXT,
    criticality ENUM('LOW','MEDIUM','HIGH','CRITICAL') DEFAULT 'MEDIUM',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Table 7: module_skills
CREATE TABLE module_skills (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    module_id      BIGINT NOT NULL,
    skill_id       BIGINT NOT NULL,
    required_level ENUM('BEGINNER','INTERMEDIATE','EXPERT') DEFAULT 'INTERMEDIATE',
    UNIQUE KEY uq_mod_skill (module_id, skill_id),
    FOREIGN KEY (module_id) REFERENCES modules(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id)  REFERENCES skills(id)  ON DELETE CASCADE
);

-- Table 8: module_owners
CREATE TABLE module_owners (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    module_id      BIGINT NOT NULL,
    employee_id    BIGINT NOT NULL,
    ownership_type ENUM('PRIMARY','BACKUP') NOT NULL,
    assigned_date  DATE DEFAULT (CURRENT_DATE),
    relieved_date  DATE,
    is_active      BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (module_id)   REFERENCES modules(id)   ON DELETE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);

-- Table 9: risk_scores
CREATE TABLE risk_scores (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    module_id      BIGINT NOT NULL,
    risk_level     ENUM('LOW','MEDIUM','HIGH','CRITICAL') NOT NULL,
    risk_score     DECIMAL(5,2) NOT NULL,
    risk_reason    TEXT,
    calculated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (module_id) REFERENCES modules(id) ON DELETE CASCADE
);