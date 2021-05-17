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

/**
 *
 * @author Saleh
 */
public class CRUDEmployee {

    Connect connect;
    Connection conn;

    public CRUDEmployee() {
        super();

        connect = new Connect();
    }

    public void insertEmp(String name, LocalDate localDate, boolean active) {
        try {

            String sql = "INSERT INTO employees (`name`,enrolled_date) VALUES (?, ?, ?)";
            conn = connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setString(1, name);
            p.setObject(2, localDate);
            p.setBoolean(3, active);
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
