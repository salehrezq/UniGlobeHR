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
import javax.swing.JOptionPane;
import model.Late;

/**
 *
 * @author Saleh
 */
public class CRUDLateAttendance {

    private static Connection conn;

    /**
     * Create Late entity as database record.
     *
     * @param lateAttendance entity to be stored into the database.
     * @param useAttendanceConnection if {@code true} then there are INSERT
     * operations for both; this Late entity and also its related
     * {@code Attendance} entity from which we use the same connection to be
     * committed as a transaction. If {@code false} then only this Late entity
     * that need to be INSERTed because its related {@code Attendance} entity
     * was already INSERTed ahead in a previous operation.
     * @return {@code int} 1 if there was successful INSERT of one record, or 0
     * if no INSERT due to some problem.
     */
    public static int create(Late lateAttendance, boolean useAttendanceConnection) {

        int create = 0;

        try {
            String sql = "INSERT INTO late (`attendance_id`, `minutes_late`) VALUES (?, ?)";
            // true input to use connection from previous connection
            // that was used for executing query for inserting attendance.
            // We use previous connection for atomic commit.
            conn = Connect.getConnection(useAttendanceConnection);
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, lateAttendance.getAttendance_id());
            p.setInt(2, lateAttendance.getMinutes_late());
            create = p.executeUpdate();

            if (create == 0) {
                JOptionPane.showConfirmDialog(null,
                        "Fail to create late attendence", "",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(CRUDLateAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return create;
    }

    public static Late getById(int lateId) {

        Late lateAttendance = null;

        try {
            String sql = "SELECT `minutes_late` FROM `late` WHERE `attendance_id` = ?";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);
            p.setInt(1, lateId);

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
            Connect.cleanUp();
        }
        return lateAttendance;
    }

    public static Late getByAttendanceId(int attendance_id) {

        Late lateAttendance = null;

        try {
            String sql = "SELECT * FROM `late` WHERE `attendance_id` = ?";
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
                lateAttendance.setId(result.getInt("id"));
                lateAttendance.setAttendance_id(attendance_id);
                lateAttendance.setMinutes_late(result.getInt("minutes_late"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDLateAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return lateAttendance;
    }

    /**
     * Update Late entity in a database record.
     *
     * @param lateAttendance entity to be used to update relevant record in the
     * database.
     * @return {@code int} 1 if there was successful UPDATE of one record, or 0
     * if no UPDATE; no update because no record was found to update or some
     * other problem.
     */
    public static int update(Late lateAttendance) {

        int update = 0;

        try {
            String sql = "UPDATE `late` SET `minutes_late`= ? WHERE `attendance_id` = ?";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, lateAttendance.getMinutes_late());
            p.setInt(2, lateAttendance.getAttendance_id());

            update = p.executeUpdate();

            conn.commit();

        } catch (SQLException ex) {
            Logger.getLogger(CRUDLateAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return update;
    }

    public static int deleteByAttendenceId(int attendanceId) {

        int deletedCount = 0;
        try {
            String sql = "DELETE FROM `late` WHERE `attendance_id` = ? LIMIT 1";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);
            p.setInt(1, attendanceId);
            deletedCount = p.executeUpdate();

            if (deletedCount == 1) {
                conn.commit();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDLateAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            Connect.cleanUp();
        }
        return deletedCount;
    }

}
