package datalink;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.SalaryAdvance;

/**
 *
 * @author Saleh
 */
public class CRUDSalaryAdvance {

    private static Connection conn;

    public static boolean create(SalaryAdvance salaryAdvance) {

        int insert = 0;

        try {
            String sql = "INSERT INTO salary_advances ("
                    + "`employee_id`, `subject_year_month`, `date_given`, `amount`)"
                    + " VALUES (?, ?, ?, ?)";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, salaryAdvance.getEmployeeId());
            p.setObject(2, salaryAdvance.getYearMonthSubject());
            p.setObject(3, salaryAdvance.getDateGiven());
            p.setBigDecimal(4, salaryAdvance.getAmount());
            insert = p.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            Logger.getLogger(CRUDSalaryAdvance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return insert > 0;
    }

    public static SalaryAdvance getById(int id) {

        SalaryAdvance salaryAdvance = null;
        try {
            String sql = "SELECT * FROM `salary_advances` WHERE id = ? LIMIT 1";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet result = p.executeQuery();

            while (result.next()) {
                salaryAdvance = new SalaryAdvance();
                salaryAdvance.setId(id);
                salaryAdvance.setEmployeeId(result.getInt("employee_id"));
                salaryAdvance.setYearMonthSubject(result.getObject("subject_year_month", LocalDate.class));
                salaryAdvance.setDateGiven(result.getObject("date_given", LocalDate.class));
                salaryAdvance.setAmount(result.getBigDecimal("amount"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDSalaryAdvance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return salaryAdvance;
    }

    public static List getSalaryAdvancesRecordByEmployeeByMonth(int employeeID, YearMonth ym) {

        List<SalaryAdvance> salaryAdvancesList = new ArrayList<>();

        try {
            LocalDate firstOfThisMonth = ym.atDay(1);
            LocalDate firstOfNextMonth = ym.plusMonths(1).atDay(1);

            String sql = "SELECT * FROM `salary_advances`"
                    + " WHERE `employee_id` = ? AND `subject_year_month` >= ? AND `subject_year_month` < ?"
                    + " ORDER BY `date_given` ASC";

            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, employeeID);
            p.setObject(2, firstOfThisMonth);
            p.setObject(3, firstOfNextMonth);

            ResultSet result = p.executeQuery();

            while (result.next()) {

                SalaryAdvance salaryAdvance = new SalaryAdvance();
                salaryAdvance.setId(result.getInt("id"));
                salaryAdvance.setEmployeeId(result.getInt("employee_id"));
                salaryAdvance.setYearMonthSubject(result.getObject("subject_year_month", LocalDate.class));
                salaryAdvance.setDateGiven(result.getObject("date_given", LocalDate.class));
                salaryAdvance.setAmount(result.getBigDecimal("amount"));
                salaryAdvancesList.add(salaryAdvance);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDSalaryAdvance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return salaryAdvancesList;
    }

    public static BigDecimal getSalaryAdvancesRecordByEmployeeByMonthAggregated(int employeeID, YearMonth ym) {

        BigDecimal salaryAdvancesAggregated = null;

        try {
            LocalDate firstOfThisMonth = ym.atDay(1);
            LocalDate firstOfNextMonth = ym.plusMonths(1).atDay(1);

            String sql = "SELECT SUM(amount) as advances_sum FROM `salary_advances`"
                    + " WHERE `employee_id` = ? AND `subject_year_month` >= ? AND `subject_year_month` < ?";

            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, employeeID);
            p.setObject(2, firstOfThisMonth);
            p.setObject(3, firstOfNextMonth);

            ResultSet result = p.executeQuery();
            boolean isRecordAvailable = result.isBeforeFirst();

            if (isRecordAvailable) {
                result.next();
                salaryAdvancesAggregated = result.getBigDecimal("advances_sum");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDSalaryAdvance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return (salaryAdvancesAggregated == null) ? BigDecimal.ZERO : salaryAdvancesAggregated;
    }

    public static boolean update(SalaryAdvance salaryAdvance) {

        int update = 0;

        try {
            String sql = "UPDATE `salary_advances` "
                    + "SET "
                    + "`subject_year_month`= ?, "
                    + "`date_given` = ?, "
                    + "`amount` = ? "
                    + "WHERE `id` = ?";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setObject(1, salaryAdvance.getYearMonthSubject());
            p.setObject(2, salaryAdvance.getDateGiven());
            p.setBigDecimal(3, salaryAdvance.getAmount());
            p.setInt(4, salaryAdvance.getId());

            update = p.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            Logger.getLogger(CRUDSalaryAdvance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return update > 0;
    }

    /**
     * To update the {@code locked } attribute of the relation to {@code true}
     * when salary is paid. This method uses the passed connection to execute
     * update on it to benefit from atomic commit for data consistency sake.
     *
     * @param conn
     * @param employeeId
     * @param yearMonthSubject
     * @throws SQLException
     */
    protected static void lockRelatedSalaryAdvance(Connection conn, int employeeId, LocalDate yearMonthSubject) throws SQLException {
        String sqlLockSalaryAdvance = "UPDATE `salary_advances` SET `locked` = true "
                + "WHERE `employee_id` = ? AND `subject_year_month` >= ? AND `subject_year_month` < ?";

        LocalDate firstOfThisMonth = H.getYearMonth(yearMonthSubject).atDay(1);
        LocalDate firstOfNextMonth = H.getYearMonth(yearMonthSubject).plusMonths(1).atDay(1);

        PreparedStatement lockSalaryAdvance = conn.prepareStatement(sqlLockSalaryAdvance);
        lockSalaryAdvance.setInt(1, employeeId);
        lockSalaryAdvance.setObject(2, firstOfThisMonth);
        lockSalaryAdvance.setObject(3, firstOfNextMonth);
        lockSalaryAdvance.executeUpdate();
    }

    public static boolean delete(Integer id) {

        int delete = 0;

        try {
            String sql = "DELETE FROM `salary_advances` WHERE `id` = ? LIMIT 1";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, id);

            delete = p.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            Logger.getLogger(CRUDSalaryAdvance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return delete > 0;
    }

}
