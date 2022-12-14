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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

@WebServlet(name = "LogoutManager", initParams = {})
public class ManagerLogoutServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(EmployeeService.class.getName());
    private ObjectMapper om = new ObjectMapper();
    @Override
    public void init() throws ServletException {
        out.println("Initializing Logout Servlet");
        om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /**
     * Request to logout manager
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Cookie cookie = new Cookie("loginM", "true");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        resp.getWriter().write(om.writeValueAsString("Manager Logged Out"));
        logger.debug("Manager logged out successfully");
    }
}
