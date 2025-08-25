import java.sql.*;
import java.util.Scanner;

public class EmployeeManagement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Connect to Oracle
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@boopal:1521:XE",
                "hr",
                "admin"
            );

            System.out.println("✅ Connected to Oracle Database!");

            while (true) {
                System.out.println("\n===== Employee Management System =====");
                System.out.println("1. View All Employees");
                System.out.println("2. Add Employee");
                System.out.println("3. Update Employee Salary");
                System.out.println("4. Delete Employee");
                System.out.println("5. Search Employee by ID");
                System.out.println("6. Search Employees by Department");
                System.out.println("7. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1: viewEmployees(con); break;
                    case 2: addEmployee(con, sc); break;
                    case 3: updateEmployee(con, sc); break;
                    case 4: deleteEmployee(con, sc); break;
                    case 5: searchEmployeeById(con, sc); break;
                    case 6: searchEmployeesByDept(con, sc); break;
                    case 7:
                        con.close();
                        System.out.println("👋 Exiting...");
                        return;
                    default:
                        System.out.println("❌ Invalid choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 1. View employees
    public static void viewEmployees(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM employee ORDER BY emp_id");
        System.out.println("\n--- Employee List ---");
        while (rs.next()) {
            System.out.println(
                rs.getInt("emp_id") + " | " +
                rs.getString("emp_name") + " | " +
                rs.getDouble("salary") + " | " +
                rs.getString("dept_name")
            );
        }
    }

    // 2. Add employee
    public static void addEmployee(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter ID: ");
        int id = sc.nextInt();

        // Check duplicate ID
        PreparedStatement check = con.prepareStatement("SELECT emp_id FROM employee WHERE emp_id = ?");
        check.setInt(1, id);
        ResultSet rs = check.executeQuery();
        if (rs.next()) {
            System.out.println("❌ Employee with ID " + id + " already exists!");
            return;
        }

        System.out.print("Enter Name: ");
        String name = sc.next();
        System.out.print("Enter Salary: ");
        double salary = sc.nextDouble();

        if (salary <= 0) {
            System.out.println("❌ Salary must be greater than 0.");
            return;
        }

        System.out.print("Enter Dept: ");
        String dept = sc.next();

        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO employee (emp_id, emp_name, salary, dept_name) VALUES (?, ?, ?, ?)"
        );
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setDouble(3, salary);
        ps.setString(4, dept);

        int rows = ps.executeUpdate();
        System.out.println("✅ " + rows + " employee(s) added.");
    }

    // 3. Update employee
    public static void updateEmployee(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to Update Salary: ");
        int id = sc.nextInt();
        System.out.print("Enter New Salary: ");
        double salary = sc.nextDouble();

        if (salary <= 0) {
            System.out.println("❌ Salary must be greater than 0.");
            return;
        }

        PreparedStatement ps = con.prepareStatement(
            "UPDATE employee SET salary = ? WHERE emp_id = ?"
        );
        ps.setDouble(1, salary);
        ps.setInt(2, id);

        int rows = ps.executeUpdate();
        if (rows > 0)
            System.out.println("✅ Employee salary updated.");
        else
            System.out.println("❌ Employee with ID " + id + " not found.");
    }

    // 4. Delete employee
    public static void deleteEmployee(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to Delete: ");
        int id = sc.nextInt();

        PreparedStatement ps = con.prepareStatement(
            "DELETE FROM employee WHERE emp_id = ?"
        );
        ps.setInt(1, id);

        int rows = ps.executeUpdate();
        if (rows > 0)
            System.out.println("✅ Employee deleted.");
        else
            System.out.println("❌ Employee with ID " + id + " not found.");
    }

    // 5. Search employee by ID
    public static void searchEmployeeById(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to Search: ");
        int id = sc.nextInt();

        PreparedStatement ps = con.prepareStatement(
            "SELECT * FROM employee WHERE emp_id = ?"
        );
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("\n✅ Employee Found: " +
                rs.getInt("emp_id") + " | " +
                rs.getString("emp_name") + " | " +
                rs.getDouble("salary") + " | " +
                rs.getString("dept_name")
            );
        } else {
            System.out.println("❌ Employee with ID " + id + " not found.");
        }
    }

    // 6. Search employees by department
    public static void searchEmployeesByDept(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Department Name to Search: ");
        String dept = sc.next();

        PreparedStatement ps = con.prepareStatement(
            "SELECT * FROM employee WHERE dept_name = ?"
        );
        ps.setString(1, dept);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n--- Employees in " + dept + " Department ---");
        boolean found = false;
        while (rs.next()) {
            System.out.println(
                rs.getInt("emp_id") + " | " +
                rs.getString("emp_name") + " | " +
                rs.getDouble("salary") + " | " +
                rs.getString("dept_name")
            );
            found = true;
        }

        if (!found) {
            System.out.println("❌ No employees found in " + dept + " department.");
        }
    }
}
