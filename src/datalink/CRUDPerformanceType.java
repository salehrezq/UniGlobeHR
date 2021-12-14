package datalink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PerformanceType;

/**
 *
 * @author Saleh
 */
public class CRUDPerformanceType {

    private static Connection conn;

    public static List getByState(boolean state) {

        List<PerformanceType> performanceTypesList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM `performance_types`"
                    + " WHERE `state` = ?";

            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setBoolean(1, state);

            ResultSet result = p.executeQuery();

            while (result.next()) {
                PerformanceType performanceType = new PerformanceType();
                performanceType.setId(result.getInt("id"));
                performanceType.setType(result.getString("type"));
                performanceType.setState(result.getBoolean("state"));
                performanceTypesList.add(performanceType);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDPerformanceType.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return performanceTypesList;
    }

    public static PerformanceType getById(int typeId) {

        PerformanceType performanceType = null;
        try {
            String sql = "SELECT * FROM `performance_types`"
                    + " WHERE `id` = ?";

            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, typeId);

            ResultSet result = p.executeQuery();

            while (result.next()) {
                performanceType = new PerformanceType();
                performanceType.setId(result.getInt("id"));
                performanceType.setType(result.getString("type"));
                performanceType.setState(result.getBoolean("state"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDPerformanceType.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return performanceType;
    }
}
