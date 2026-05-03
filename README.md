# 🎉 Event Management System

## 📌 Description
This is a Java Swing-based desktop application connected to a MySQL database.  
It allows users to manage events, register users, and assign users to specific events.

---

## 🚀 Features
- Add new events
- Register users
- Assign users to events
- View events in a table
- Maintain relational database using MySQL

---

## 🛠️ Technologies Used
- Java (Swing for GUI)
- MySQL (Database)
- JDBC (Java Database Connectivity)

---

## 🗄️ Database Structure

### Tables:
- **users** → Stores user details
- **events** → Stores event details
- **registrations** → Links users with events
- **schedules** → Stores event schedule details

---

## ▶️ How to Run

### 1. Compile the program
```bash
javac -cp ".;mysql-connector-j-9.7.0/*" EventSystem.java
