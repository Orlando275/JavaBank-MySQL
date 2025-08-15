<!-- Banner -->
<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=rect&color=gradient&height=100&section=header&text=JavaBank-MySQL&fontSize=40&animation=fadeIn" />
</p>

## 🎯 Project Overview

JavaBank-MySQL is a Java-based desktop banking system where you can:

- 🔐 **Securely log in or register** with password hashing and database validation.  
- 💳 **Manage accounts** with real-time deposits, withdrawals, and balance checks.  
- 📈 **View transaction history** with sortable and filterable records for each user.  
- 🖥️ **Navigate seamlessly** through a multi-window Java Swing interface.  
- 🛠 **Easily extend features** thanks to a clean, modular MVC architecture.  

This project is designed to showcase robust Java application development, database integration with MySQL, and professional-grade UI/UX design in a multi-window desktop environment.

---

## 📥 Installation & Setup

1. Clone this repository

    ```bash
    git clone https://github.com/Orlando275/JavaBank-MySQL.git
    ```

2. Configure your Java project

    - If you’re using Maven, add the MySQL Connector dependency in your `pom.xml`.
    - If you’re using Gradle, include the connector in your `build.gradle`.
    - Or download the JDBC driver JAR of your choice, place it in a `lib/` directory, and add it to your classpath.

3. Build and run

    ```bash
    # With Maven
    mvn clean install
    mvn exec:java -Dexec.mainClass="com.mycompany.project_.Main"

    # With Gradle
    gradle build
    gradle run
    ```

---

## 💾 Database Setup

Create the `project_bank` database and `users` table by executing the following SQL (via MySQL Workbench, CLI, etc.):

```sql
CREATE DATABASE project_bank;
USE project_bank;

CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  email    VARCHAR(255),
  password VARCHAR(255) NOT NULL
);
```
---

## 🔌 JDBC Driver Installation

- Download the JDBC driver for your database (e.g., MySQL Connector/J).  
- Place the JAR in your project’s `lib/` folder or register it as a Maven/Gradle dependency.  
- Ensure the driver is on the runtime classpath so the application can connect to `project_bank`.  

---

## ✨ Features

- 🔐 **User Authentication** – Secure login and registration with password hashing and database validation.  
- 💳 **Account Management** – Deposit, withdrawal, and balance overview for each user.  
- 📈 **Transaction History** – Persistent log of transactions, sortable and filterable for quick review.  
- 🖥️ **Multi-Window UI** – Intuitive Java Swing interfaces with responsive layouts.  
- 🛠️ **Modular Architecture** – Clear separation of controllers, DAOs, models, and views for easy extension and maintenance.  

---

## 📂 Project Structure
<pre>
PROJECT_BANK/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── mycompany/
│   │               └── project_/
│   │                   ├── LoginWindow.java
│   │                   ├── Main.java
│   │                   ├── MainPage.java
│   │                   └── RegisterWindow.java
│   └── test/
│       └── java/
├── target/
├── LICENSE
├── pom.xml
└── README.md
</pre>

---

## 🖼️ Screenshots

### Welcome Screen
![Welcome Screen](https://github.com/user-attachments/assets/64233549-1493-4d5b-a2a1-1b70c3b73b7e)

### Registration Form
![Registration Form](https://github.com/user-attachments/assets/919dbd67-2e56-4040-acd2-e4650c3db507)

### Login Window
![Login Window](https://github.com/user-attachments/assets/e8c81cc7-3501-4a5f-a637-d1ac4404b8ae)

### Login Success Popup
![Login Success](https://github.com/user-attachments/assets/51b02fd6-e5db-410d-bd9b-f7d11e2c47d5)

### Main Page Dashboard
![Main Dashboard](https://github.com/user-attachments/assets/3895a5b2-a178-4f90-b50a-b4f8aa2a719c)

---

## 🚀 How It Works

- **Startup** – `Main.java` initializes the Swing UI and establishes a JDBC connection to `project_bank`.  
- **Authentication** – User credentials are validated against the `users` table; new users can register.  
- **Account Operations** – Deposit and withdrawal actions update the database and UI in real time.  
- **Transaction Logging** – Every operation is recorded and fetched for display in the history view.  
- **Modularity** – Controllers handle user interactions, DAOs manage SQL queries, and models represent core entities — making feature additions straightforward.  

---

## 🛠️ Technologies Used

- **Java 11+**  
- **Java Swing** (UI)  
- **JDBC / MySQL Connector/J**  
- **MySQL 8**  
- **Maven** or **Gradle**

---

## 🔮 Future Improvements

- Add role-based access control (admin vs. customer)  
- Encrypt sensitive data at rest  
- Integrate real-time notifications *(e.g., balance alerts)*  
- Build a web front-end with Spring Boot

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.  

