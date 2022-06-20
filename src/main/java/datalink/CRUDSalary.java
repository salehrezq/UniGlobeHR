package datalink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Salary;

/**
 *
 * @author Saleh
 */
public class CRUDSalary {

    private static Connection conn;

    public static boolean create(Salary salary) {

        int insert = 0;

        try {
            String sql = "INSERT INTO salaries ("
                    + "`employee_id`, `subject_year_month`, `date_given`,`agreed_salary` ,`payable`)"
                    + " VALUES (?, ?, ?, ?, ?)";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, salary.getEmployeeId());
            p.setObject(2, salary.getYearMonthSubject());
            p.setObject(3, salary.getDateGiven());
            p.setBigDecimal(4, salary.getAgreedSalary());
            p.setBigDecimal(5, salary.getPayable());
            insert = p.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            Logger.getLogger(CRUDSalary.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return insert > 0;
    }

    public static Salary isEmployeeWithYearMonthSubjectExist(int employeeId, LocalDate yearMonthSubject) {

        Salary salary = null;

        try {
            String sql
                    = "SELECT * FROM `salaries` "
                    + "WHERE `employee_id` = ? "
                    + "AND `subject_year_month` = ?";

            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);
            p.setInt(1, employeeId);
            p.setObject(2, yearMonthSubject);

            ResultSet result = p.executeQuery();

            // Check if there is a result
            if (result.next()) {
                salary = new Salary();
                salary.setId(result.getInt("id"));
                salary.setEmployeeId(result.getInt("employee_id"));
                salary.setYearMonthSubject(result.getDate("subject_year_month").toLocalDate());
                salary.setDateGiven(result.getDate("date_given").toLocalDate());
                salary.setAgreedSalary(result.getBigDecimal("agreed_salary"));
                salary.setPayable(result.getBigDecimal("payable"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(CRUDSalary.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return salary;
    }

    public static Salary getById(int id) {
        throw new UnsupportedOperationException("CRUDSalary getById placeholder");
    }

    public static boolean update(Salary salaryAdvance) {
        throw new UnsupportedOperationException("CRUDSalary update placeholder");
    }

    public static boolean delete(Integer id) {
        throw new UnsupportedOperationException("CRUDSalary delete placeholder");
    }

}
