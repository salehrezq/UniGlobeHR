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
import javax.swing.JOptionPane;
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

    /**
     * Inserts <code>Attendance</code> entity to the database, and returns the
     * state of creation.
     *
     * @param attendance that will feed the method with the attendance state of
     * employee
     * @param eas that will be updated with the create state of attendance
     * @return <code>EmployeeAttendanceStatus</code> that contains the create
     * state of attendance.
     */
    private static EmployeeAttendanceStatus create(Attendance attendance, EmployeeAttendanceStatus eas) {

        int create = 0;

        try {
            String sql = "INSERT INTO attendance (`employee_id`, `date`, `state`) VALUES (?, ?, ?)";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, attendance.getEmployeeId());
            p.setObject(2, attendance.getDate());
            p.setBoolean(3, attendance.getStateOfAttendance());
            create = p.executeUpdate();
            eas.setCreatedOrFailed(create);
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
        return eas;
    }

    /**
     * Updates <code>Attendance</code> entity in the database, and returns the
     * state of updating.
     *
     * @param attendance that will feed the method with the attendance state of
     * employee
     * @param eas that will be updated with the update state of attendance.
     * @return <code>EmployeeAttendanceStatus</code> that contains the update
     * state of attendance.
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
            eas.setUpdatedOrFailed(update);
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
        return eas;
    }

    /**
     * Takes the attendance using either create or update, if input state of
     * attendance is the same as the stores, then no update is needed.
     *
     * @param attendance that will feed the method with the attendance state of
     * employee
     * @return <code>EmployeeAttendanceStatus</code> that contains the state of
     * attendance create/update process
     */
    public static EmployeeAttendanceStatus takeAttendance(Attendance attendance) {

        EmployeeAttendanceStatus eas = getEmployeeAttendanceStatusOnSpecificDate(attendance.getEmployeeId(), attendance.getDate());

        if (eas.getWasAttendanceTaken() == false) {
            // If attendance was not taken, then take it
            // and inform EmployeeAttendanceStatus about the state
            create(attendance, eas);
            if (!eas.getCreateState()) {
                JOptionPane.showConfirmDialog(null,
                        "Fail to create attendance", "",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Attendance already was taken
            if (eas.getEmployeeAttendanceState() == attendance.getStateOfAttendance()) {
                // No need to update since the input the same as the stored value.
                eas.setWhetherUpdateNeeded(false);
            } else {
                // input is different from the stored value, so we need to update it.
                update(attendance, eas);
                eas.setWhetherUpdateNeeded(true);
                if (!eas.getUpdateState()) {
                    JOptionPane.showConfirmDialog(null,
                            "Fail to update attendance", "",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                }
            }
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
                // Move cursor to next record, which is the first in this case.
                result.next();
                eas.setWhetherAttendanceWasTaken(ATTENDANCE_ALREADY_TAKEN);
                eas.setEmployeeAttendanceState(eas.retrieveAttendanceStateFromResultSet(result.getBoolean("state")));
            } else {
                eas.setWhetherAttendanceWasTaken(ATTENDANCE_NOT_TAKEN);
                eas.setEmployeeAttendanceState(ATTENDANCE_STATE_NOT_SET);
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

        private void setWhetherAttendanceWasTaken(int isTaken) {
            if (isTaken == ATTENDANCE_ALREADY_TAKEN) {
                this.isAttendanceTaken = true;
            } else if (isTaken == ATTENDANCE_NOT_TAKEN) {
                this.isAttendanceTaken = false;
            }
        }

        public boolean getWasAttendanceTaken() {
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

        /**
         * Helper method for setEmployeeAttendanceState()
         *
         * @param state
         * @return <code>int</code>
         */
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

        private void setCreatedOrFailed(int createdOrFailed) {
            if (createdOrFailed == 1) {
                this.created = ATTENDANCE_CREATED;
            } else {
                this.created = FAIL_ON_CREATE_OR_UPDATE;
            }
        }

        private boolean getCreateState() {
            if (this.created == ATTENDANCE_CREATED) {
                return true;
            }
            return false;
        }

        private void setUpdatedOrFailed(int updatedOrFailed) {
            if (updatedOrFailed == 1) {
                this.updated = ATTENDANCE_UPDATED;
            } else {
                this.updated = FAIL_ON_CREATE_OR_UPDATE;
            }
        }

        private boolean getUpdateState() {
            if (this.updated == ATTENDANCE_UPDATED) {
                return true;
            }
            return false;
        }

        public boolean getWhetherUpdateNeeded() {
            return isUpdateNeeded;
        }

        private void setWhetherUpdateNeeded(boolean isUpdateNeeded) {
            this.isUpdateNeeded = isUpdateNeeded;
        }
    }

}
