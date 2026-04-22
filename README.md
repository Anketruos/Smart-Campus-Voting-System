# 🗳️ Smart Campus Voting System

The **Smart Campus Voting System** is a Java-based application that allows university students to vote online without needing to travel to their home locations. It makes the voting process easier, faster, and more organized.

---

## 📌 Features

- Student registration and login  
- Admin creates and manages elections  
- Admin adds candidates  
- Students vote online  
- Each student can vote only once  
- View election results  

---

## 🏗️ System Structure

The system follows a simple structure:

UI (JavaFX)  
↓  
Controller  
↓  
Service  
↓  
DAO (Database)  
↓  
MySQL  

---

## 🧩 Modules

- **Authentication** → Login and Registration  
- **Election** → Create and manage elections  
- **Candidate** → Add candidates to elections  
- **Voting** → Students vote  
- **Result** → Display results  
- **Admin** → Manage the system  

---

## 🗄️ Database Tables

- voters  
- elections  
- candidates  
- votes  
- preapproved_voters  

---

## ⚙️ Technologies Used

- Java  
- JavaFX  
- MySQL  
- JDBC  

---

## 📁 Project Structure

- app/ → Main application  
- ui/ → User interface  
- controller/ → Handles actions  
- service/ → Logic  
- dao/ → Database  
- model/ → Data classes  
- util/ → Helper tools  
- database/ → SQL files  

---

## 🔄 How It Works

1. User clicks button (UI)  
2. Controller handles action  
3. Service applies rules  
4. DAO interacts with database  
5. Result is shown  

---

## 👥 Team Members

| ID         | Name                  | GitHub |
|------------|----------------------|--------|
| ETS0229/16 | Beamlak Atilabachew | https://github.com/Beamlak-Atilabachew |
| ETS0222/16 | Barkot Frew        | https://github.com/Barkotfrew |
| ETS0223/16 | Barkot Zerihun     | https://github.com/Barkot-Zerihun |
| ETS0192/16 | Arsema Seife       | https://github.com/Arsema-Seife |
| ETS0076/16 | Abigiya Fikru      | https://github.com/AbigiyaFikru |
| ETS0201/16 | Aster Teshome      | https://github.com/Anketruos |

---

## 🚀 How to Run

1. Clone the project  
2. Open in IntelliJ or Eclipse  
3. Import database from `schema.sql`  
4. Add MySQL connector (JAR file)  
5. Run `MainApp.java`  

---

## 📄 Note

- Each voter can vote only once  
- Admin controls elections and candidates  
- This project is for educational purposes  

---
