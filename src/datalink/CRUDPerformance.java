package datalink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Attendance;
import model.Late;
import model.Performance;

/**
 *
 * @author Saleh
 */
public class CRUDPerformance {

    private static Connection conn;

    public static boolean create(Performance performance) {

        int insert = 0;

        try {
            String sql = "INSERT INTO performance ("
                    + "`employee_id`, `date_time`, `type_id`,"
                    + " `state`, `amount`, `title`, `description` )"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?)";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, performance.getEmployeeId());
            p.setObject(2, performance.getDateTime());
            p.setInt(3, performance.getTypeId());
            p.setBoolean(4, performance.getState());
            p.setDouble(5, performance.getAmount());
            p.setString(6, performance.getTitle());
            p.setString(7, performance.getDescription());
            insert = p.executeUpdate();
            conn.commit();

        } catch (SQLException ex) {
            Logger.getLogger(CRUDPerformance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return insert > 0;
    }

    public static List getPerformanceRecordByEmployeeByMonth(int employeeID, YearMonth ym) {

        List<Performance> performanceList = new ArrayList<>();

        try {
            LocalDate firstOfThisMonth = ym.atDay(1);
            LocalDate firstOfNextMonth = ym.plusMonths(1).atDay(1);

            String sql = "SELECT * FROM `performance`"
                    + " WHERE `employee_id` = ? AND `date_time` >= ? AND `date_time` < ?"
                    + " ORDER BY `date_time` ASC";

            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, employeeID);
            p.setObject(2, firstOfThisMonth);
            p.setObject(3, firstOfNextMonth);

            ResultSet result = p.executeQuery();

            while (result.next()) {

                Performance performance = new Performance();
                performance.setId(result.getInt("id"));
                performance.setEmployeeId(result.getInt("employee_id"));
                performance.setState(result.getBoolean("state"));
                performance.setAmount(result.getDouble("amount"));
                performance.setTitle(result.getString("title"));
                performance.setDescription(result.getString("description"));
                performance.setDateTime(result.getObject("date_time", LocalDateTime.class));
                performanceList.add(performance);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDPerformance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return performanceList;
    }

}
