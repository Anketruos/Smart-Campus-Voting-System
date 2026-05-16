# Smart Campus Voting System

A JavaFX desktop application for campus elections. Students register with pre-approved campus credentials, log in, view active elections, and cast exactly one vote per election. Administrators manage elections, candidates, approved voters, and view real-time results.

## Main Features

- Student registration with pre-approval checks
- Secure login for students and admin users
- Active-election dashboard with candidate viewing and vote casting
- One-vote-per-election enforcement
- Admin election and candidate management
- Real-time result viewing using Java streams and lambdas
- SQLite database with JDBC and automatic schema initialization
- Serializable candidate data utility
- Socket server and RMI server examples for networking units
- Synchronized voting logic for concurrency protection

## Project Structure

```text
SmartCampusVotingSystem-JavaFX/
|-- database/
|   `-- schema.sql
|-- lib/
|   |-- javafx/
|   `-- sqlite-jdbc-<version>.jar
|-- src/
|   |-- app/
|   |-- controller/
|   |-- dao/
|   |-- model/
|   |-- network/
|   |-- rmi/
|   |-- service/
|   |-- ui/
|   |-- util/
|   `-- resources/
|-- compile.bat
|-- run-app.bat
|-- run-rmi-server.bat
`-- run-socket-server.bat
```

## Technologies Used

- Java 17
- JavaFX
- JDBC
- SQLite
- Java serialization
- Java sockets
- Java RMI
- Multithreading and synchronization

## Running the Project

1. Place the JavaFX jars inside `SmartCampusVotingSystem-JavaFX/lib/javafx/`.
2. Place the SQLite JDBC jar inside `SmartCampusVotingSystem-JavaFX/lib/`.
3. Run `run-app.bat`.

The application automatically creates `database/campus_voting.db` and loads the schema from `src/resources/schema.sql` on first run.

## Default Credentials

- Admin username: `admin`
- Admin password: `admin123`

## Sample Approved Students

- `STU001` / `student1@campus.edu`
- `STU002` / `student2@campus.edu`
- `STU003` / `student3@campus.edu`
- `STU004` / `student4@campus.edu`
- `STU005` / `student5@campus.edu`

Students still create their own passwords during registration.

## Unit Coverage

| Unit | Topic | Where It Appears |
|------|-------|------------------|
| 1 | Functional programming, lambdas, streams, immutability | `ResultService`, immutable `Vote` |
| 2 | Packages, collections, serialization | DAO collections, `BallotSerializer` |
| 3 | JDBC and persistence | `DBConnection` and all DAO classes |
| 4 | Networking and client-server | `network/VotingServer`, `VotingClient` |
| 5 | RMI | `rmi/VotingRemote`, `VotingRemoteImpl`, `VotingRMIServer` |
| 6 | Multithreading and synchronization | synchronized vote casting, threaded socket server |
| 7 | GUI | JavaFX screens in `src/ui/` |

## Notes for Demonstration

- Use the admin account to pre-approve new students if needed.
- Create or edit elections from the admin dashboard.
- Add candidates before voting starts.
- Students can only vote while the election is active and within its date range.
- Results update from the stored vote records, not from temporary memory.
