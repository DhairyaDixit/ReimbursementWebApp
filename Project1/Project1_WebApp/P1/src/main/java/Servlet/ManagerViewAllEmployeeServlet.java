package Servlet;

import JDBC.ConnectionManager;
import JDBC.EmployeeDao;
import Model.Employee;
import Model.EmployeeResponse;
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

@WebServlet(name="ViewAllEmployee", initParams = {})
public class ManagerViewAllEmployeeServlet extends HttpServlet {
    private ObjectMapper om = new ObjectMapper();
    private static Logger logger = LogManager.getLogger(EmployeeService.class.getName());

    @Override
    public void init() throws ServletException {
        out.println("Initializing Servlet");
        om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /**
     * Request to view all employee by manager
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        out.println(req.getRequestURI());

        logger.debug("Manager requested to view all employee");

        HttpSession userSession = req.getSession(true);

        Cookie[] allCookies = req.getCookies();

        Cookie cookieM = null;
        for (Cookie cookieN : allCookies) {
            if (cookieN.getName().equalsIgnoreCase("loginM")) {
                cookieN.getValue();
                cookieM = new Cookie(cookieN.getName(), cookieN.getValue());
                break;
            }
        }

        if (cookieM != null) {
            out.println("cookie 1 value " + cookieM.getValue());
            out.println("cookie 1 name " + cookieM.getName());

            if (cookieM.getValue().equals("true")) {

                ConnectionManager cm = (ConnectionManager) getServletContext().getAttribute("database");

                EmployeeDao employeeDao = new EmployeeDao(cm);
                ManagerService ms = new ManagerService(employeeDao);

                List<Employee> employee = new ArrayList<>(ms.viewAllEmployees());

                EmployeeResponse eList = new EmployeeResponse();
                eList.setEmpList(employee);

                    try {
                        resp.setContentType("application/json");
                        resp.getWriter().write(om.writeValueAsString(eList));
                        resp.setStatus(200);
                        logger.debug("Manager could view all employee");
                    } catch (JsonProcessingException e) {
                    } catch (IOException ioException) {
                    }
                }
            else{
                resp.setStatus(403);
                resp.getWriter().write(om.writeValueAsString("Manager Not Logged In to Access this Facility"));
            }
            }
        }
}
