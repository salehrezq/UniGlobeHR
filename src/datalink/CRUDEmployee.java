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
import java.util.SortedSet;
import java.util.TreeSet;
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
    }

    public static SortedSet getAll() {

        SortedSet<Employee> sortedEmployees = new TreeSet<>((emplyee1, employee2) -> {
            if (emplyee1 instanceof Employee && employee2 instanceof Employee) {
                String name1 = emplyee1.getName();
                String name2 = employee2.getName();
                return name1.compareTo(name2);
            }
            return 0; //To change body of generated lambdas, choose Tools | Templates.
        });

        try {

            String sql = "SELECT * FROM `employees` WHERE `active` = 1";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);
            ResultSet result = p.executeQuery();

            while (result.next()) {
                Employee employee = new Employee();
                employee.setId(result.getInt("id"));
                employee.setName(result.getString("name"));
                employee.setEnrolledDate(result.getDate("enrolled_date").toLocalDate());
                employee.setActive(result.getBoolean("active"));
                sortedEmployees.add(employee);
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
        return sortedEmployees;
    }
}
