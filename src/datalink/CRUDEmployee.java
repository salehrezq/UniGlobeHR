/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datalink;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class CRUDEmployee {

    private static Connection conn;

    public static boolean create(Employee employee) {

        int insert = 0;

        try {

            String sql = "INSERT INTO employees (`name`, `enrolled_date`, `active`) VALUES (?, ?, ?)";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setString(1, employee.getName());
            p.setObject(2, employee.getEnrolledDate());
            p.setBoolean(3, employee.isActive());
            insert = p.executeUpdate();
            conn.commit();

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
        return insert > 0;
    }

    public static ArrayList<Employee> getAll() {

        ArrayList<Employee> employees = new ArrayList<>();

        try {

            String sql = "SELECT * FROM `employees` WHERE `active` = 1 ORDER BY `name` ASC";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);
            ResultSet result = p.executeQuery();

            while (result.next()) {
                Employee employee = new Employee();
                employee.setId(result.getInt("id"));
                employee.setName(result.getString("name"));
                employee.setEnrolledDate(result.getDate("enrolled_date").toLocalDate());
                employee.setActive(result.getBoolean("active"));
                employees.add(employee);
            }
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
        return employees;
    }
}
