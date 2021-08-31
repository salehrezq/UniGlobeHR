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
    private static final int ATTENDANCE_ALREADY_TAKEN = -1;
    private static final int ATTENDANCE_NOT_TAKEN = 2;
    private static final int ATTENDANCE_STATE_PRESENT = 3;
    private static final int ATTENDANCE_STATE_ABSENT = 4;
    private static final int ATTENDANCE_STATE_NOT_SET = ATTENDANCE_NOT_TAKEN;
    private static final int ATTENDANCE_CREATED = 5;
    private static final int ATTENDANCE_UPDATED = 6;
    private static final int FAIL_ON_CREATE_OR_UPDATE = 7;

    private static EmployeeAttendanceStatus create(Attendance attendance, EmployeeAttendanceStatus eas) {

        int insert = 0;

        try {
            String sql = "INSERT INTO attendance (`employee_id`, `date`, `state`) VALUES (?, ?, ?)";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, attendance.getEmployeeId());
            p.setObject(2, attendance.getDate());
            p.setBoolean(3, attendance.getStateOfAttendance());
            insert = p.executeUpdate();
            eas.setCreated(insert);
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
        return eas;

    }

    /**
     * Receives Attendance object, and returns 0 or 1.0 => no record updated 1 =
     * > one record has been successfully updated 0 if no record updated, or 1
     * if one record has been successfully updated
     *
     * @param attendance
     * @param eas
     * @return EmployeeAttendanceStatus
     */
    private static EmployeeAttendanceStatus update(Attendance attendance, EmployeeAttendanceStatus eas) {

        int update = 0;

        try {
            String sql = "UPDATE `attendance` SET `state`= ? WHERE (`employee_id` = ? AND `date` = ?)";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setBoolean(1, attendance.getStateOfAttendance());
            p.setInt(2, attendance.getEmployeeId());
            p.setObject(3, attendance.getDate());
            update = p.executeUpdate();
            eas.setUpdated(update);
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
        return eas;
    }

    public static EmployeeAttendanceStatus takeAttendance(Attendance attendance) {

        EmployeeAttendanceStatus eas = getEmployeeAttendanceStatusOnSpecificDate(attendance.getEmployeeId(), attendance.getDate());

        if (eas.getWasAttendanceBeenTaken() == false) {
            create(attendance, eas);
        } else {
            if (eas.getEmployeeAttendanceState() == attendance.getStateOfAttendance()) {
                eas.setWhetherUpdateNeeded(false);
            } else {
                update(attendance, eas);
                eas.setWhetherUpdateNeeded(true);
            }
            return null;
        }
        return eas;
    }

    /**
     *
     * @param employeeId
     * @param date
     * @return
     */
    public static EmployeeAttendanceStatus getEmployeeAttendanceStatusOnSpecificDate(int employeeId, LocalDate date) {

        EmployeeAttendanceStatus eas = new EmployeeAttendanceStatus();

        try {
            String sql = "SELECT `state` FROM `attendance` WHERE `employee_id` = ? AND `date` = ?";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);
            p.setInt(1, employeeId);
            p.setObject(2, date);

            ResultSet result = p.executeQuery();

            // Check if there is a result
            boolean isRecordAvailable = result.isBeforeFirst();

            if (isRecordAvailable) {

                // Move cursor to first record
                result.next();

                eas.setWhetherAttendanceBeenTaken(ATTENDANCE_ALREADY_TAKEN);
                // either true or false
                eas.setEmployeeAttendanceState(eas.retrieveAttendanceStateFromResultSet(result.getBoolean("state")));
            } else {
                eas.setWhetherAttendanceBeenTaken(ATTENDANCE_NOT_TAKEN);
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

        return eas;
    }

//    public static boolean isEmployeeAbsentAtSpecificDate(int employee_id, LocalDate date) {
//
//        boolean employeeAbsentAtSpecificDate = false;
//
//        try {
//            String sql = "SELECT * FROM `attendance` WHERE `employee_id` = ? AND `date` = ? AND `state` = ?";
//            conn = Connect.getConnection();
//            PreparedStatement p = conn.prepareStatement(sql);
//            p.setInt(1, employee_id);
//            p.setObject(2, date);
//            p.setBoolean(3, false);
//            ResultSet result = p.executeQuery();
//
//            // Check if there is a result
//            if (result.isBeforeFirst()) {
//                employeeAbsentAtSpecificDate = true;
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(CRUDAttendance.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (SQLException se) {
//                se.printStackTrace();
//            }
//        }
//        return employeeAbsentAtSpecificDate;
//    }
    public static List getAbsenceRecordByEmployeeByMonth(int employeeID, YearMonth ym) {

        List records = new ArrayList<>();

        try {

            //  YearMonth yearMonth = YearMonth.of(ym.getYear(), ym.getMonthValue());
            LocalDate firstOfThisMonth = ym.atDay(1);
            LocalDate firstOfNextMonth = ym.plusMonths(1).atDay(1);

            String sql = "SELECT `date` FROM `attendance` WHERE `employee_id` = ? AND `date` >= ? AND `date` < ? AND `state` = ?";
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

    public static class EmployeeAttendanceStatus {

        private boolean isAttendanceTaken;
        private Boolean employeeAttendanceState;
        private int created;
        private int updated;
        private boolean isUpdateNeeded;

        private void setWhetherAttendanceBeenTaken(int isTaken) {
            if (isTaken == ATTENDANCE_ALREADY_TAKEN) {
                this.isAttendanceTaken = true;
            } else if (isTaken == ATTENDANCE_NOT_TAKEN) {
                this.isAttendanceTaken = false;
                this.setEmployeeAttendanceState(ATTENDANCE_STATE_NOT_SET);
            }
        }

        public boolean getWasAttendanceBeenTaken() {
            return this.isAttendanceTaken;
        }

        private void setEmployeeAttendanceState(int attendanceState) {

            if (attendanceState == ATTENDANCE_STATE_PRESENT) {
                this.employeeAttendanceState = Boolean.TRUE;
            } else if (attendanceState == ATTENDANCE_STATE_ABSENT) {
                this.employeeAttendanceState = Boolean.FALSE;
            } else if (attendanceState == ATTENDANCE_STATE_NOT_SET) {
                employeeAttendanceState = null;
            }
        }

        private int retrieveAttendanceStateFromResultSet(boolean state) {
            if (state) {
                return ATTENDANCE_STATE_PRESENT;
            } else {
                return ATTENDANCE_STATE_ABSENT;
            }
        }

        public Boolean getEmployeeAttendanceState() {
            return employeeAttendanceState;
        }

        private void setCreated(int created) {
            if (created == 1) {
                this.created = ATTENDANCE_CREATED;
            } else {
                this.created = FAIL_ON_CREATE_OR_UPDATE;
            }
        }

        private int getCreated() {
            return created;
        }

        private void setUpdated(int updated) {
            if (updated == 1) {
                this.updated = ATTENDANCE_UPDATED;
            } else {
                this.updated = FAIL_ON_CREATE_OR_UPDATE;
            }
        }

        private int getUpdated() {
            return updated;
        }

        public boolean getWhetherUpdateNeeded() {
            return isUpdateNeeded;
        }

        private void setWhetherUpdateNeeded(boolean isUpdateNeeded) {
            this.isUpdateNeeded = isUpdateNeeded;
        }

    }

}
