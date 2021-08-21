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
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Attendance;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class CRUDAttendance {

    private static Connection conn;

    public static boolean create(Attendance attendance) {

        int insert = -1;

        try {

            String sql = "INSERT INTO attendance (`employeeID`, `date`) VALUES (?, ?)";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, attendance.getEmployeeId());
            p.setObject(2, attendance.getDate());
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
        return insert > 0;
    }
}
