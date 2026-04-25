-- Smart Campus Voting System Database Schema

CREATE DATABASE IF NOT EXISTS smart_campus_voting;
USE smart_campus_voting;

CREATE TABLE IF NOT EXISTS admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS preapproved_voters (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    registered BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS voters (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    has_voted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS elections (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS candidates (
    id INT AUTO_INCREMENT PRIMARY KEY,
    election_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    position VARCHAR(100),
    bio TEXT,
    FOREIGN KEY (election_id) REFERENCES elections(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS votes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    voter_id INT NOT NULL,
    election_id INT NOT NULL,
    candidate_id INT NOT NULL,
    timestamp DATETIME NOT NULL,
    UNIQUE KEY unique_vote (voter_id, election_id),
    FOREIGN KEY (voter_id) REFERENCES voters(id),
    FOREIGN KEY (election_id) REFERENCES elections(id),
    FOREIGN KEY (candidate_id) REFERENCES candidates(id)
);

-- Default admin credentials (username: admin, password: admin123)
-- Hash is SHA-256 of "admin123"
INSERT IGNORE INTO admins (username, password_hash)
VALUES ('admin', 'jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=');

-- Sample pre-approved voters (student must use matching student_id + email to register)
INSERT IGNORE INTO preapproved_voters (student_id, email) VALUES
('STU001', 'student1@campus.edu'),
('STU002', 'student2@campus.edu'),
('STU003', 'student3@campus.edu'),
('STU004', 'student4@campus.edu'),
('STU005', 'student5@campus.edu');

-- Sample election
INSERT IGNORE INTO elections (id, title, description, start_date, end_date, active) VALUES
(1, 'Student Council Election 2026', 'Vote for your student council president.', '2026-04-01', '2026-04-30', TRUE);

-- Sample candidates for the election
INSERT IGNORE INTO candidates (election_id, name, position, bio) VALUES
(1, 'Alice Johnson', 'President', 'Committed to improving campus facilities.'),
(1, 'Bob Smith', 'President', 'Focused on student welfare and academic support.'),
(1, 'Carol White', 'President', 'Advocating for more student events and clubs.');
