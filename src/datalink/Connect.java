/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datalink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Saleh
 */
public class Connect {

    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/uniglobehr";

    //  Database credentials
    private static final String USER = "root";
    private static final String PASS = "1212";

    // For atomic statements
    private static Connection conn;

    public static Connection getConnection() {
        cleanUp();
        conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public static Connection getConnection(boolean usePreviousConnectionForAtomicCommit) {

        if (conn != null && usePreviousConnectionForAtomicCommit) {
            return conn;
        }

        conn = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public static void cleanUp() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
