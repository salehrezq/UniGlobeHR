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
            String sqlCreateSalary = "INSERT INTO salaries ("
                    + "`employee_id`, `subject_year_month`, `date_given`,`agreed_salary` ,`payable`)"
                    + " VALUES (?, ?, ?, ?, ?)";

            conn = Connect.getConnection();
            PreparedStatement createSalary = conn.prepareStatement(sqlCreateSalary);

            createSalary.setInt(1, salary.getEmployeeId());
            createSalary.setObject(2, salary.getYearMonthSubject());
            createSalary.setObject(3, salary.getDateGiven());
            createSalary.setBigDecimal(4, salary.getAgreedSalary());
            createSalary.setBigDecimal(5, salary.getPayable());
            insert = createSalary.executeUpdate();

            CRUDAttendance.lockRelatedAttendance(conn, salary.getEmployeeId(), salary.getYearMonthSubject());
            CRUDPerformance.lockRelatedPerformance(conn, salary.getEmployeeId(), salary.getYearMonthSubject());
            CRUDSalaryAdvance.lockRelatedSalaryAdvance(conn, salary.getEmployeeId(), salary.getYearMonthSubject());

            conn.commit();

        } catch (SQLException ex) {
            Logger.getLogger(CRUDSalary.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            Connect.rollBack();
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

    public static boolean delete(Salary salary) {

        int delete = 0;

        try {
            String sql = "DELETE FROM `salary` WHERE `id` = ? LIMIT 1";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, salary.getId());

            delete = p.executeUpdate();

            CRUDAttendance.unlockRelatedAttendance(conn, salary.getEmployeeId(), salary.getYearMonthSubject());
            CRUDPerformance.unlockRelatedPerformance(conn, salary.getEmployeeId(), salary.getYearMonthSubject());
            CRUDSalaryAdvance.unlockRelatedSalaryAdvance(conn, salary.getEmployeeId(), salary.getYearMonthSubject());

            conn.commit();

        } catch (SQLException ex) {
            Logger.getLogger(CRUDPerformance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.rollBack();
            Connect.cleanUp();
        }
        return delete > 0;
    }

}
