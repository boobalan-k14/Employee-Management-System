--query to create table

 create table employee (
  emp_id number primary key,
  emp_name varchar(30),
  salary number,
  dept_name varchar(60)
);

-- to insert the values 

INSERT INTO Employee (emp_id, emp_name, salary, dept_name)
values (1,'Arun',30000,'HR')
insert into employee (emp_id, emp_name, salary, dept_name) values (2,'Kumar',45000,'IT')
insert into employee (emp_id, emp_name, salary, dept_name) values (3,'Priya',50000,'FINANCE')


-- procedure for application

CREATE OR REPLACE PROCEDURE hike_salary (
    p_id NUMBER,
    p_percent NUMBER
) AS
BEGIN
    UPDATE Employee
    SET salary = salary + (salary * p_percent / 100)
    WHERE emp_id = p_id;
END;
/

-- to work the procedure

SQL> EXEC hike_salary(2, 10);

-- java program to connect with database mini version

import java.sql.*;

public class OracleConnect {
    public static void main(String[] args) {
        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Connection details
            String url = "jdbc:oracle:thin:@boopal:1521:XE";  // Host: boopal, Port: 1521, Service: XE
            String username = "hr";  
            String password = "admin"; 

            // Connect to Oracle
            Connection con = DriverManager.getConnection(url, username, password);

            System.out.println("✅ Connected to Oracle!");

            // Execute SQL
            Statement stmt = con.createStatement();

               int rows = stmt.executeUpdate(
                "UPDATE employee SET salary = salary + 5000 WHERE dept_name = 'IT'");

          
          // 2. Query again to see the result
            ResultSet rs = stmt.executeQuery("SELECT * FROM employee");

            // Print output
            while (rs.next()) {
                System.out.println(
                    rs.getInt("emp_id") + " | " +
                    rs.getString("emp_name") + " | " +
                    rs.getDouble("salary") + " | " +
                    rs.getString("dept_name")
                );
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

-- COMPILATION CMD

java -cp .;ojdbc11.jar EmployeeManagement

-- RUN COMMAND 

java -cp .;ojdbc11.jar EmployeeManagement