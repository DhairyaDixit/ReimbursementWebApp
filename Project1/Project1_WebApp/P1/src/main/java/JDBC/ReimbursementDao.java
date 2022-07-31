package JDBC;

import Model.Employee;
import Model.Reimbursement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementDao {

    private ConnectionManager cm;

    public ReimbursementDao(ConnectionManager cm) {
        this.cm = cm;
    }

    public boolean submitReimbursementRequest(int empid, Reimbursement r) {

        Connection connection = null;

        try {
            String QUERY = "insert into reimbursement(empid, amount, bankaccountno, status) values(?,?,?,?)";

            // insert into reimbursement(empid, amount, bankaccountno, status) values(1, 500.00, '000111123', 'Pending');

            connection = cm.getConnection();

            if(connection != null) {
                System.out.println("Connection established in reimbursementdao"); }

            PreparedStatement stmt = connection.prepareStatement(QUERY);

            stmt.setInt(1,empid);
            stmt.setDouble(2,r.getReimburseAmount());
            stmt.setString(3,r.getBankAccount());
            stmt.setString(4,"Pending");

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

    }

    public List<Reimbursement> viewResolvedReimbursementRequest(int empid){

        Connection connection = null;
        List<Reimbursement> resolvedReimbursementRequest = new ArrayList<>();

        try{
            String QUERY = "select * from reimbursement where empid = ? and status = 'Approved' or status = 'Denied'";

            connection = cm.getConnection();
            PreparedStatement stmt = connection.prepareStatement(QUERY);

            stmt.setInt(1,empid);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                //Display values
                System.out.println("\n");
                System.out.print("ID: " + rs.getInt("empid"));

               Reimbursement r = new Reimbursement();
                r.setEmpID(rs.getInt("empid"));
                r.setReimbursmentID(rs.getInt("requestid"));
                r.setManagerID(rs.getInt("managerid"));
                r.setReimburseAmount(rs.getDouble("amount"));
                r.setBankAccount(rs.getString("bankaccountno"));
                r.setStatus(rs.getString("status"));
                resolvedReimbursementRequest.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resolvedReimbursementRequest;
    }

    public List<Reimbursement> viewPendingReimbursementRequest(int empid){

        Connection connection = null;
        List<Reimbursement> resolvedReimbursementRequest = new ArrayList<>();

        try{
            String QUERY = "select * from reimbursement where empid = ? and status = 'Pending'";

            connection = cm.getConnection();
            PreparedStatement stmt = connection.prepareStatement(QUERY);

            stmt.setInt(1,empid);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                //Display values
                System.out.println("\n");
                System.out.print("ID: " + rs.getInt("empid"));

                Reimbursement r = new Reimbursement();
                r.setEmpID(rs.getInt("empid"));
                r.setReimbursmentID(rs.getInt("requestid"));
                r.setManagerID(rs.getInt("managerid"));
                r.setReimburseAmount(rs.getDouble("amount"));
                r.setBankAccount(rs.getString("bankaccountno"));
                r.setStatus(rs.getString("status"));
                resolvedReimbursementRequest.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resolvedReimbursementRequest;
    }

    public List<Reimbursement> ViewPendingRequestAllEmployee(){

        Connection connection = null;
        List<Reimbursement> pendingReimbursementRequest = new ArrayList<>();

        try{
            String QUERY = "select * from reimbursement where status = 'Pending'";

            connection = cm.getConnection();
            PreparedStatement stmt = connection.prepareStatement(QUERY);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                //Display values
                System.out.println("\n");
                System.out.print("ID: " + rs.getInt("empid"));

                Reimbursement r = new Reimbursement();
                r.setEmpID(rs.getInt("empid"));
                r.setReimbursmentID(rs.getInt("requestid"));
                r.setManagerID(rs.getInt("managerid"));
                r.setReimburseAmount(rs.getDouble("amount"));
                r.setBankAccount(rs.getString("bankaccountno"));
                r.setStatus(rs.getString("status"));
                pendingReimbursementRequest.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pendingReimbursementRequest;
    }

    public List<Reimbursement> viewResolvedReimbursementRequest(){

        Connection connection = null;
        List<Reimbursement> resolvedReimbursementRequest = new ArrayList<>();

        try{
            String QUERY = "select * from reimbursement where status = 'Approved' or status = 'Denied'";

            connection = cm.getConnection();
            PreparedStatement stmt = connection.prepareStatement(QUERY);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                //Display values
                System.out.println("\n");
                System.out.print("ID: " + rs.getInt("empid"));

                Reimbursement r = new Reimbursement();
                r.setEmpID(rs.getInt("empid"));
                r.setReimbursmentID(rs.getInt("requestid"));
                r.setManagerID(rs.getInt("managerid"));
                r.setReimburseAmount(rs.getDouble("amount"));
                r.setBankAccount(rs.getString("bankaccountno"));
                r.setStatus(rs.getString("status"));
                resolvedReimbursementRequest.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resolvedReimbursementRequest;
    }

    public boolean processReimbursementRequest(int requestid, int managerid, String status){
        Connection connection = null;

        try {
            String QUERY = "update reimbursement set managerid = ?, status = ? where requestid = ?";

            connection = cm.getConnection();
            PreparedStatement stmt = connection.prepareStatement(QUERY);

            stmt.setInt(1,managerid);
            stmt.setString(2,status);
            stmt.setInt(3,requestid);

            stmt.executeUpdate();

            return true;

        } catch (SQLException es) {
            throw new RuntimeException(es);

        }
    }


}
