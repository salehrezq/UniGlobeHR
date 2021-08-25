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
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Attendance;

/**
 *
 * @author Saleh
 */
public class CRUDAttendance {

    private static Connection conn;
    private static final int ALREADY_INSERTED = -1;

    public static int create(Attendance attendance) {

        int insert = 0;

        try {

            if (CRUDAttendance.isEmployeeAbsentAtSpecificDate(attendance.getEmployeeId(), attendance.getDate())) {
                return ALREADY_INSERTED;
            }

            String sql = "INSERT INTO attendance (`employeeID`, `date`, `state`) VALUES (?, ?, ?)";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, attendance.getEmployeeId());
            p.setObject(2, attendance.getDate());
            p.setBoolean(3, false);
            insert = p.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(CRUDAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        /**
         * 0 means no insertion 1 means one record has been inserted
         */
        return insert;
    }

    public static boolean isEmployeeAbsentAtSpecificDate(int employeeID, LocalDate date) {

        boolean employeeAbsentAtSpecificDate = false;

        try {
            String sql = "SELECT * FROM `attendance` WHERE `employeeID` = ? AND `date` = ? AND `state` = ?";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);
            p.setInt(1, employeeID);
            p.setObject(2, date);
            p.setBoolean(3, false);
            ResultSet result = p.executeQuery();

            // Check if there is a result
            if (result.isBeforeFirst()) {
                employeeAbsentAtSpecificDate = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return employeeAbsentAtSpecificDate;
    }

    public static List getAbsenceRecordByEmployeeByMonth(int employeeID, YearMonth ym) {

        List records = new ArrayList<>();

        try {

            //  YearMonth yearMonth = YearMonth.of(ym.getYear(), ym.getMonthValue());
            LocalDate firstOfThisMonth = ym.atDay(1);
            LocalDate firstOfNextMonth = ym.plusMonths(1).atDay(1);

            String sql = "SELECT `date` FROM `attendance` WHERE `employeeID` = ? AND `date` >= ? AND `date` < ? AND `state` = ?";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, employeeID);
            p.setObject(2, firstOfThisMonth);
            p.setObject(3, firstOfNextMonth);
            p.setBoolean(4, false);

            ResultSet result = p.executeQuery();

            Object[] record = null;

            while (result.next()) {

                LocalDate resultDate = result.getDate("date").toLocalDate();
                record = new Object[2];
                record[0] = resultDate.getDayOfWeek().toString();
                record[1] = resultDate.getDayOfMonth();
                records.add(record);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return records;
    }
}
