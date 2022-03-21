package datalink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
                    + "`employee_id`, `subject_year_month`, `date_taken`, `amount`)"
                    + " VALUES (?, ?, ?, ?)";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, salaryAdvance.getEmployeeId());
            p.setObject(2, salaryAdvance.getYearMonthSubject());
            p.setObject(3, salaryAdvance.getDateTaken());
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

    public static boolean update(SalaryAdvance salaryAdvance) {

        int update = 0;

        try {
            String sql = "UPDATE `salary_advances` "
                    + "SET "
                    + "`subject_year_month`= ?, "
                    + "`date_taken` = ?, "
                    + "`amount` = ?, "
                    + "WHERE id = ?";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setObject(1, salaryAdvance.getYearMonthSubject());
            p.setObject(2, salaryAdvance.getDateTaken());
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

}
