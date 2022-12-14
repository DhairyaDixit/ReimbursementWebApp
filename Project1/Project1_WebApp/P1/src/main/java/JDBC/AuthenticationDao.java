package JDBC;

import Model.Employee;
import Model.Manager;
import Service.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationDao {

    private ConnectionManager cm;
    public AuthenticationDao(ConnectionManager cm) {
        this.cm = cm;
    }

    private static Logger logger = LogManager.getLogger(EmployeeService.class.getName());

    /**
     * Authenticates the Manager Account's login
     * @param login login ID of the manager
     * @param password password of the manager
     * @return Manager object if manager present in the database
     */
    public Manager loginAuthenticationManager(String login, String password) {

        Connection connection = null;
        Manager manager = new Manager();

        try{
            String QUERY = "SELECT * FROM manager where login = ? AND password = ? ";  //check login syntax

            connection = cm.getConnection();
            PreparedStatement stmt = connection.prepareStatement(QUERY);

            stmt.setString(1,login.trim());
            stmt.setString(2,password.trim());
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                //Display values
                manager.setManagerID(rs.getInt("managerid"));
                manager.setFirstName(rs.getString("fname"));
                manager.setLastName(rs.getString("lname"));
                manager.setLoginID(rs.getString("login"));
                manager.setEmailID(rs.getString("emailid"));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("Manager Logged In");
       return manager;
    }

    /**
     * Authenticates the Employee Login
     * @param login Login ID of Employee
     * @param password password of Employee
     * @return Employee object if its present in the database
     */
    public Employee loginAuthenticationEmployee(String login, String password) {

        Connection connection = null;
        Employee employee = new Employee();

        try{
            String QUERY = "SELECT * FROM employee where login = ? AND password = ? ";  //check login syntax

            connection = cm.getConnection();
            PreparedStatement stmt = connection.prepareStatement(QUERY);

            stmt.setString(1,login.trim());
            stmt.setString(2,password.trim());
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {

                employee.setManagerID(rs.getInt("managerid"));
                employee.setFirstName(rs.getString("fname"));
                employee.setLastName(rs.getString("lname"));
                employee.setLoginID(rs.getString("login"));
                employee.setEmailID(rs.getString("emailid"));
                employee.setEmpID(rs.getInt("empid"));
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("Employee Logged In");
        return employee;
    }
}
