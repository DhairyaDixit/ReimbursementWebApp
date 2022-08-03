package Servlet;

import JDBC.AuthenticationDao;
import JDBC.ConnectionManager;
import JDBC.EmployeeDao;
import JDBC.ReimbursementDao;
import Model.Employee;
import Model.EmployeeResponse;
import Model.Manager;
import Model.Reimbursement;
import Service.EmployeeService;
import Service.ManagerService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

@WebServlet(name ="Authentication" , initParams = {})
public class EmployeeAuthenticationServlet extends HttpServlet {

    private ObjectMapper om = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        out.println("Initializing Servlet");
        om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession userSession = req.getSession(true);


            Employee employee = om.readValue(req.getInputStream(), Employee.class);

            ConnectionManager cm = (ConnectionManager) getServletContext().getAttribute("database");

            AuthenticationDao authenticationDao = new AuthenticationDao(cm);
            EmployeeService es = new EmployeeService(authenticationDao);

            Employee e = es.employeeLogin(employee.getLoginID(), employee.getPassword());

            userSession.setAttribute("empid", e.getEmpID());

            if (e != null) {

                out.println("Login Successful" + e.getFirstName() + " " + e.getLastName());
                Cookie cookie = new Cookie("login", "true");
                cookie.setMaxAge(7 * 24 * 60 * 60);
                resp.addCookie(cookie);

            }

            resp.setStatus(201);
    }
}