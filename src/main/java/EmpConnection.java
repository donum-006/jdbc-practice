import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpConnection {
    public static final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/mydb");
    public static final String USER = System.getenv().getOrDefault("DB_USER", "postgres");
    public static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "your_password");
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL,USER,PASSWORD)){
            System.out.println("Connected to Postgres successfully!");
            //create table(recreate)
            resetTable(conn);
            //insert data
            insertRowData(conn);
            List<Employee> employees = getAllEmployees(conn);
            printTable("List Of Initial Employees",employees);
            //update Employees
            updateEmployees(conn);
            List<Employee> afterUpdated = getAllEmployees(conn);
            printTable("List Of Employees After Update",afterUpdated);
            // delete employees
            deleteEmployee(conn);
            List<Employee> afterDeletion = getAllEmployees(conn);
            printTable("List Of Employees",afterDeletion);

        } catch (SQLException e) {
            System.out.println("Error while connecting Postgres: " + e.getMessage());
        }

    }
    private static void resetTable(Connection conn)  {
        String sql = """
                DROP TABLE IF EXISTS employees;
                CREATE TABLE IF NOT EXISTS employees (
                    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    name VARCHAR(30) NOT NULL,
                    position VARCHAR(30) NOT NULL,
                    salary NUMERIC(10,2)
                    );
                """;
        try (Statement  stmt = conn.createStatement()){
            stmt.executeUpdate(sql);
            System.out.println("Table recreated and reset.");


        } catch (SQLException e) {
            System.out.println("Error while creating table: " + e.getMessage());
        }
    }
    private static void insertRowData(Connection conn) {
        String sql = """
                INSERT INTO employees(name,position,salary)
                VALUES (?,?,?);
                """;
        try (PreparedStatement prpstmt = conn.prepareStatement(sql)){
            // Row 1
            prpstmt.setString(1,"Kena");
            prpstmt.setString(2,"Backend Developer");
            prpstmt.setDouble(3,2000);
            prpstmt.addBatch();
            // Row 2
            prpstmt.setString(1,"fra");
            prpstmt.setString(2,"Frontend Developer");
            prpstmt.setDouble(3,3000);
            prpstmt.addBatch();

            //Row 3
            prpstmt.setString(1,"Sena");
            prpstmt.setString(2,"Developer");
            prpstmt.setDouble(3,3000);
            prpstmt.addBatch();

            // Row 4
            prpstmt.setString(1,"Wabi");
            prpstmt.setString(2,"Full Stack Developer");
            prpstmt.setDouble(3,4000);
            prpstmt.addBatch();

            //Row 5
            prpstmt.setString(1,"Jeni");
            prpstmt.setString(2,"Cyber Specialist");
            prpstmt.setDouble(3,5000);
            prpstmt.addBatch();

            int[] results = prpstmt.executeBatch();
            System.out.println("Inserted " + results.length + " initial row(s).");
        } catch (SQLException e) {
            System.out.println("Error while inserting data: " + e.getMessage());
        }
    }
    private static List<Employee> getAllEmployees(Connection conn)  {
        List<Employee> employeeList = new ArrayList<>();
        String sql = """
                SELECT id, name, position, salary
                FROM employees
                ORDER BY salary DESC;
                """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String position = rs.getString("position");
                double salary = rs.getDouble("salary");
                employeeList.add(new Employee(id, name, position, salary));
            }
        } catch (SQLException e) {
            System.out.println("Error while getting data from database: " + e.getMessage());
        }
        return  employeeList;
    }
    private static void updateEmployees(Connection conn)  {
        String sql = """
                UPDATE employees SET salary = ? WHERE id = ?;
                """;
        try (PreparedStatement prpstmt = conn.prepareStatement(sql)){
            prpstmt.setDouble(1,6000);
            prpstmt.setInt(2,2);

            int rowUpdated = prpstmt.executeUpdate();
            System.out.println(rowUpdated + " row(s) are updated." );

        } catch (SQLException e) {
            System.out.println("Error while updating data: " + e.getMessage());
        }
    }
    private static void deleteEmployee(Connection conn) {
        String sql = """
                DELETE FROM employees WHERE id = ?;
                """;
        try (PreparedStatement prpstmt = conn.prepareStatement(sql)){
            prpstmt.setInt(1,2);
            int rowAffected = prpstmt.executeUpdate();
            System.out.println(rowAffected + " row(s) deleted.");
        } catch (SQLException e) {
            System.out.println("Error while deleting data from database: " + e.getMessage());
        }
    }
    public static void printTable(String title, List<Employee> empList) {
        String headFormat = "| %-5s | %-20s | %-20s | %14s |%n";
        String dataFormat = "| %-5d | %-20s | %-20s | %,14.2f |%n";
        String border = "+" + "-".repeat(68) + "+";

        System.out.println("\n" + border);
        System.out.printf("| %-66s |%n",title);
        System.out.println(border);
        System.out.printf(headFormat,"NO", "NAME", "POSITION", "SALARY");
        System.out.println(border);
        int rowNumber = 1;
        for(Employee emp : empList) {
            System.out.printf(dataFormat,rowNumber++, emp.name(), emp.position(), emp.salary());
            System.out.println(border);
        }
    }
}
