package Servlet;

import JDBC.ConnectionManager;
import JDBC.EmployeeDao;
import Model.BankAccount;
import Model.Employee;
import Model.EmployeeResponse;
import Model.Reimbursement;
import Service.EmployeeService;
import Service.ManagerService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;


@WebServlet(name="ViewEmployee", initParams = {})
public class ViewEmployeeServlet extends HttpServlet {

    private ObjectMapper om = new ObjectMapper();
    private static Logger logger = LogManager.getLogger(EmployeeService.class.getName());
    @Override
    public void init() throws ServletException {
        out.println("Initializing Servlet");
        om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /**
     * Request to view profile information of employee
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        out.println(req.getRequestURI());

        logger.debug("View all employee requested");

        String requestType = req.getRequestURI().split("/")[3];

        out.println("value of reqest type in view empl :::: "+requestType);
        HttpSession userSession = req.getSession(true);

        int empid = (int) userSession.getAttribute("empid");

        Cookie[] allCookies = req.getCookies();
        Cookie cookie = null;

        for (Cookie cookieN : allCookies) {
            if (cookieN.getName().equalsIgnoreCase("login")) {
                cookieN.getValue();
                cookie = new Cookie(cookieN.getName(), cookieN.getValue());
                break;
            }
        }

        Cookie cookieM = null;
        for (Cookie cookieN : allCookies) {
            if (cookieN.getName().equalsIgnoreCase("login")) {
                cookieN.getValue();
                cookieM = new Cookie(cookieN.getName(), cookieN.getValue());
                break;
            }
        }

        if (cookie != null) {
            out.println("cookie 1 value " + cookie.getValue());
            out.println("cookie 1 name " + cookieM.getName());

            if (cookie.getValue().equals("true")) {

                ConnectionManager cm = (ConnectionManager) getServletContext().getAttribute("database");

                EmployeeDao employeeDao = new EmployeeDao(cm);

                if (requestType.equalsIgnoreCase("profile_update")) {

                    out.println("updating through view employee servlet");

                } else {
                    List<Employee> employee = new ArrayList<>();

                    if (requestType.equalsIgnoreCase("profile_view")) {
                        EmployeeService es = new EmployeeService(employeeDao);
                        employee = es.viewProfileInformation(empid);
                    } else if (requestType.equalsIgnoreCase("all")) {

                            ManagerService ms = new ManagerService(employeeDao);
                            employee = ms.viewAllEmployees();
                    }

                    EmployeeResponse eList = new EmployeeResponse();
                    eList.setEmpList(employee);

                    try {
                        resp.setContentType("application/json");
                        resp.getWriter().write(om.writeValueAsString(eList));
                        resp.setStatus(200);
                        logger.debug("All employee returned");
                    } catch (JsonProcessingException e) {
                    } catch (IOException ioException) {
                    }
                }
            }
        } else {
            resp.setStatus(403);
            resp.getWriter().write(om.writeValueAsString("Manager Not Logged In to Access this Facility"));
        }
    }

    /**
     * Requests to update profile information
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Cookie[] allCookies = req.getCookies();
        Cookie cookie = null;

        HttpSession userSession = req.getSession(true);
        int empid = (int) userSession.getAttribute("empid");

        String requestType = req.getRequestURI().split("/")[3];

        for (Cookie cookieN : allCookies) {
            if (cookieN.getName().equalsIgnoreCase("login")) {
                cookieN.getValue();
                cookie = new Cookie(cookieN.getName(), cookieN.getValue());
                break;
            }
        }

        if (cookie != null) {
            out.println("cookie 1 value " + cookie.getValue());
            out.println("cookie 1 name " + cookie.getName());

            if (cookie.getValue().equals("true")) {

                if (requestType.equalsIgnoreCase("profile_update")) {

                    Employee employee = om.readValue(req.getInputStream(), Employee.class);

                    ConnectionManager cm = (ConnectionManager) getServletContext().getAttribute("database");
                    EmployeeDao employeeDao = new EmployeeDao(cm);

                    EmployeeService es = new EmployeeService(employeeDao);

                    es.updateProfileInformation(employee, empid);
                    out.println("Employee profile updated");
                }

            }
        } else {
            resp.setStatus(403);
            resp.getWriter().write(om.writeValueAsString("Manager Not Logged In to Access this Facility"));
        }
    }
}

