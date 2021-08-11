/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datalink;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class CRUDEmployee {

    private static Connection conn;

    public static void create(Employee employee) {
        try {

            String sql = "INSERT INTO employees (`name`, `enrolled_date`, `active`) VALUES (?, ?, ?)";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setString(1, employee.getName());
            p.setObject(2, employee.getEnrolledDate());
            p.setBoolean(3, employee.isActive());
            p.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(CRUDEmployee.class.getName()).log(Level.SEVERE, null, ex);
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
}
