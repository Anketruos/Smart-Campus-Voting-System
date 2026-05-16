CREATE TABLE IF NOT EXISTS admins (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS preapproved_voters (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    registered INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS voters (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id TEXT NOT NULL UNIQUE,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    has_voted INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS elections (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT,
    start_date TEXT NOT NULL,
    end_date TEXT NOT NULL,
    active INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS candidates (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    election_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    position TEXT NOT NULL,
    bio TEXT,
    FOREIGN KEY (election_id) REFERENCES elections(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS votes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    voter_id INTEGER NOT NULL,
    election_id INTEGER NOT NULL,
    candidate_id INTEGER NOT NULL,
    timestamp TEXT NOT NULL,
    UNIQUE (voter_id, election_id),
    FOREIGN KEY (voter_id) REFERENCES voters(id),
    FOREIGN KEY (election_id) REFERENCES elections(id) ON DELETE CASCADE,
    FOREIGN KEY (candidate_id) REFERENCES candidates(id) ON DELETE CASCADE
);

INSERT OR IGNORE INTO admins (username, password_hash)
VALUES ('admin', 'JAvlGPq9JyTdtvBO6x2llnRI1+gxwIyPqCKAn3THIKk=');

INSERT OR IGNORE INTO preapproved_voters (student_id, email, registered) VALUES
('STU001', 'student1@campus.edu', 0),
('STU002', 'student2@campus.edu', 0),
('STU003', 'student3@campus.edu', 0),
('STU004', 'student4@campus.edu', 0),
('STU005', 'student5@campus.edu', 0);

INSERT OR IGNORE INTO elections (id, title, description, start_date, end_date, active) VALUES
(1, 'Student Council Election 2026', 'Vote for the next student council president and executive team.', '2026-05-01', '2026-12-31', 1);

INSERT OR IGNORE INTO candidates (id, election_id, name, position, bio) VALUES
(1, 1, 'Alice Johnson', 'President', 'Committed to improving campus facilities and lab access.'),
(2, 1, 'Bob Smith', 'President', 'Focused on student welfare, tutoring support, and scholarships.'),
(3, 1, 'Carol White', 'President', 'Advocates for more student events, clubs, and participation.');
