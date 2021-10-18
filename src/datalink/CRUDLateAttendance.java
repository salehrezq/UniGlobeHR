/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datalink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Late;

/**
 *
 * @author Saleh
 */
public class CRUDLateAttendance {

    private static Connection conn;

    public static void create(Late lateAttendance) {

        int create = 0;

        try {
            String sql = "INSERT INTO late (`attendance_id`, `minutes_late`) VALUES (?, ?)";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, lateAttendance.getAttendance_id());
            p.setInt(2, lateAttendance.getMinutes_late());
            create = p.executeUpdate();
            // eas.setCreatedOrFailed(create);
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(CRUDLateAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static Late getLateAttendance(int attendance_id) {

        Late lateAttendance = null;

        try {
            String sql = "SELECT `minutes_late` FROM `late` WHERE `attendance_id` = ?";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);
            p.setInt(1, attendance_id);

            ResultSet result = p.executeQuery();

            // Check if there is a result
            boolean isRecordAvailable = result.isBeforeFirst();

            if (isRecordAvailable) {
                // Move cursor to next record, which is the first in this case.
                result.next();
                lateAttendance = new Late();
                lateAttendance.setMinutes_late(result.getInt("minutes_late"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDLateAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return lateAttendance;
    }

}
