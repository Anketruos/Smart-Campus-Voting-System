# Smart Campus Voting System

A JavaFX desktop application for managing campus elections. Students can register, log in, and cast votes. Admins manage elections, candidates, and voter approvals.

---

## Project Structure

```
SmartCampusVotingSystem-JavaFX/
├── database/
│   └── schema.sql          # MySQL schema + sample data
├── lib/
│   └── mysql-connector.jar # JDBC driver
└── src/
    ├── app/        MainApp.java          (entry point)
    ├── controller/ AuthController, AdminController, VoterController, VotingController, ResultController, ElectionController, CandidateController
    ├── dao/        DBConnection, VoterDAO, ElectionDAO, CandidateDAO, VoteDAO, PreapprovedVoterDAO
    ├── model/      Voter, Admin, Election, Candidate, Vote (immutable), PreapprovedVoter
    ├── network/    VotingServer, VotingClient   (Unit 4: Networking)
    ├── rmi/        VotingRemote, VotingRemoteImpl, VotingRMIServer  (Unit 5: RMI)
    ├── service/    AuthService, ElectionService, CandidateService, VotingService, ResultService
    ├── ui/         LoginUI, AdminLoginUI, RegisterUI, AdminUI, ElectionUI, VoterUI, VoteUI, ResultUI
    ├── util/       AlertUtil, BallotSerializer, PasswordUtil, SessionManager, ValidationUtil
    └── resources/  config.properties, styles.css
```

---

## Setup

### 1. Database
- Install MySQL and run `database/schema.sql`
- Edit `src/resources/config.properties` and set your MySQL password:
  ```
  db.url=jdbc:mysql://localhost:3306/smart_campus_voting
  db.user=root
  db.password=YOUR_PASSWORD
  ```

### 2. Compile & Run
Add `lib/mysql-connector.jar` and JavaFX SDK to your classpath, then run `app.MainApp`.

---

## Default Credentials

| Role  | Username | Password  |
|-------|----------|-----------|
| Admin | admin    | admin123  |

### Sample Pre-approved Voters
Register using one of these student ID + email pairs:

| Student ID | Email                  |
|------------|------------------------|
| STU001     | student1@campus.edu    |
| STU002     | student2@campus.edu    |
| STU003     | student3@campus.edu    |

---

## Unit Coverage

| Unit | Topic                          | Where Applied                                              |
|------|--------------------------------|------------------------------------------------------------|
| 1    | Lambdas, Streams, Immutability | `ResultService` (streams/lambdas), `Vote` (immutable)      |
| 2    | Serialization, Collections     | `BallotSerializer`, `ArrayList`/`HashMap` in all DAOs      |
| 3    | JDBC / Persistence             | All DAO classes, `DBConnection`                            |
| 4    | Networking / Sockets           | `VotingServer`, `VotingClient`                             |
| 5    | RMI                            | `VotingRemote`, `VotingRemoteImpl`, `VotingRMIServer`      |
| 6    | Multithreading / Synchronization | `VotingServer` (threads per client), `VotingService` (synchronized) |
| 7    | JavaFX GUI                     | All UI classes in `src/ui/`                                |
