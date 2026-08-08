# Java PostgreSQL JDBC CRUD Application

A simple, robust Java application demonstrating database interaction with PostgreSQL using JDBC (Java Database Connectivity).

## Features

* **Database Connection Management**: Establishes connections using environment variables with fallback defaults via `DriverManager`.
* **Automated Table Schema Management**: Resets and recreated the `employees` table schema safely.
* **Batch Operations**: Uses `PreparedStatement` batching (`addBatch()`, `executeBatch()`) for high-performance batch insertion.
* **Full CRUD Operations**:
  * **Create**: Batch insert initial employee records.
  * **Read**: Retrieve all records sorted by salary using `Statement` and `ResultSet`.
  * **Update**: Dynamically update employee records via `PreparedStatement`.
  * **Delete**: Safely remove records using parameterized queries.
* **Console Table Output**: Custom tabular formatting utility to display employee records clearly in the terminal.

##  Prerequisites

* **Java Development Kit (JDK)**: Version 17 or higher
* **PostgreSQL**: Running locally or hosted remotely
* **JDBC Driver**: `org.postgresql:postgresql` driver dependency

##  Configuration & Running

By default, the application attempts to connect to `jdbc:postgresql://localhost:5432/mydb`.

You can configure your connection credentials using environment variables:

```bash
export DB_URL="jdbc:postgresql://localhost:5432/your_database"
export DB_USER="your_username"
export DB_PASSWORD="your_password"
```
## Run the application:
```bash
javac EmpConnection.java Employee.java
java EmpConnection
```
##  Data Model

The `employees` table structure:

| Column | Type | Constraints |
| :--- | :--- | :--- |
| `id` | `INT` | `PRIMARY KEY`, `GENERATED ALWAYS AS IDENTITY` |
| `name` | `VARCHAR(30)` | `NOT NULL` |
| `position` | `VARCHAR(30)` | `NOT NULL` |
| `salary` | `NUMERIC(10,2)` | — |
