package Servlet;

import JDBC.ConnectionManager;
import JDBC.ReimbursementDao;
import Model.Reimbursement;
import Service.EmployeeService;
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

@WebServlet(name = "ViewReimbursementRequestEmployee",initParams = {})
public class ViewReimbursementRequestEmployee extends HttpServlet {
    private ObjectMapper om = new ObjectMapper();
    private static Logger logger = LogManager.getLogger(EmployeeService.class.getName());

    /**
     * Requests for pending and resolved reimbursement requests and for all reimbursement requests by employee ID
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        out.println("view reimbursement request"+req.getRequestURI());

        logger.debug("view Reimbursement request");

        String requestType = req.getRequestURI().split("/")[3];
        out.println("request type in view reimbursement servlet  ------- " +requestType);

        HttpSession userSession = req.getSession(true);

        Cookie[] allCookies = req.getCookies();
        Cookie cookie = null;

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

            int empid = (int) userSession.getAttribute("empid");

                out.println("Emp ID in view reimbursement request servlet == " +empid);

            ConnectionManager cm = (ConnectionManager) getServletContext().getAttribute("database");

            ReimbursementDao reimbursementDao = new ReimbursementDao(cm);
            EmployeeService es = new EmployeeService(reimbursementDao);
            List<Reimbursement> r = new ArrayList<>();

            if(requestType.equalsIgnoreCase("pending")){
                r = es.viewPendingReimbursementRequest(empid);
                logger.debug("Pending reimbursement requests returned");
            }
            else if(requestType.equalsIgnoreCase("resolved")){
                r = es.viewResolvedReimbursementRequest(empid);
                logger.debug("Resolved reimbursement requests returned");
            }
            else if(requestType == null){
                r = es.viewResolvedReimbursementRequest(empid);
                r.addAll(es.viewPendingReimbursementRequest(empid));
                logger.debug("all reimbursement requests of an employee returned");
            }

            try {
                if(r != null) {
                    resp.setContentType("application/json");
                    resp.getWriter().write(om.writeValueAsString(r));
                    resp.setStatus(200);
                }
                } catch  (JsonProcessingException e) {
                }
                catch (IOException ioException) {
                }
            }
        } else {
            resp.setStatus(403);
            resp.getWriter().write(om.writeValueAsString("Manager Not Logged In to Access this Facility"));
        }
    }
}
