/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datalink;

import java.sql.Connection;
import java.sql.Statement;
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
    private static boolean commitAndDontWaitAnotherExecuteStmt;

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
    private static EmployeeAttendanceStatus create(Attendance attendance, EmployeeAttendanceStatus eas, boolean commitAndDontWaitAnotherExecuteStmt) {

        int create = 0;

        try {
            String sql = "INSERT INTO attendance (`employee_id`, `date`, `state`) VALUES (?, ?, ?)";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //  System.out.println(attendance.getStateOfAttendance()? "present":"absent");
            p.setInt(1, attendance.getEmployeeId());
            p.setObject(2, attendance.getDate());
            p.setBoolean(3, attendance.getStateOfAttendance());
            create = p.executeUpdate();

            eas.setCreatedOrFailed(create);
            if (eas.getCreateState() == false) {
                // Failed to insert attendance record.
                JOptionPane.showConfirmDialog(null,
                        "Fail to create attendance", "",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }

            if (eas.getCreateState()) {
                // Succeeded to insert attendance record, now get the record id.
                try ( ResultSet generatedKeys = p.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        eas.setAttendanceId(generatedKeys.getInt(1));
                    } else {
                        JOptionPane.showConfirmDialog(null,
                                "Fail to create attendance inner 2", "",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            if (commitAndDontWaitAnotherExecuteStmt && eas.getCreateState()) {
                conn.commit();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (commitAndDontWaitAnotherExecuteStmt) {
                Connect.cleanUp();
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
            Connect.cleanUp();
        }
        return eas;
    }

    /**
     * Takes the attendance using either create or update, if input state of
     * attendance is the same as the stores, then no update is needed.
     *
     * @param attendance that will feed the method with the attendance state of
     * employee
     * @param commitAndDontWaitAnotherExecuteStmt that will set if connection is
     * auto committed <code>true</code> or block/atom committed
     * <code>false</code>. It will be set to <code>true</code> in normal cases,
     * and <code>false</code> when we need to execute multiple insert statements
     * which have to be either all inserted altogether successfully or fail
     * altogether. For example the case when we need to insert both attendance
     * as present and also insert a `late` record for that attendance.
     * @return <code>EmployeeAttendanceStatus</code> that contains the state of
     * attendance create/update process
     */
    public static EmployeeAttendanceStatus takeAttendance(Attendance attendance, boolean commitAndDontWaitAnotherExecuteStmt) {

        EmployeeAttendanceStatus eas = getEmployeeAttendanceStatusOnSpecificDate(attendance.getEmployeeId(), attendance.getDate());

        if (eas.getWasAttendanceTaken() == false) {
            // If attendance was not taken, then take it
            // and inform EmployeeAttendanceStatus about the state
            create(attendance, eas, commitAndDontWaitAnotherExecuteStmt);
        } else {
            // Attendance already was taken, then it is update call.
            if (eas.getEmployeeStoredAttendanceState() == attendance.getStateOfAttendance()) {
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
            String sql = "SELECT `id`, `state` FROM `attendance` WHERE `employee_id` = ? AND `date` = ?";
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
                eas.setAttendanceId(result.getInt("id"));
                eas.retrieveWhetherAttendanceWasTaken(ATTENDANCE_ALREADY_TAKEN);
                eas.retrieveEmployeeStoredAttendanceState(eas.retrieveAttendanceStoredStateFromResultSet(result.getBoolean("state")));
            } else {
                eas.retrieveWhetherAttendanceWasTaken(ATTENDANCE_NOT_TAKEN);
                eas.retrieveEmployeeStoredAttendanceState(ATTENDANCE_STATE_NOT_SET);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }

        return eas;
    }

    public static List getAbsenceRecordByEmployeeByMonth(int employeeID, YearMonth ym) {

        List records = new ArrayList<>();

        try {

            //  YearMonth yearMonth = YearMonth.of(ym.getYear(), ym.getMonthValue());
            LocalDate firstOfThisMonth = ym.atDay(1);
            LocalDate firstOfNextMonth = ym.plusMonths(1).atDay(1);

            String sql = "SELECT `date` FROM `attendance` WHERE `employee_id` = ? AND `date` >= ? AND `date` < ? AND `state` = ?  ORDER BY `date` ASC";
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
            Connect.cleanUp();
        }
        return records;
    }

    public static class EmployeeAttendanceStatus {

        private int attendanceId;
        private boolean isAttendanceTaken;
        private Boolean employeeStoredAttendanceState;
        private int created;
        private int updated;
        private boolean isUpdateNeeded;

        public void setAttendanceId(int attendanceId) {
            this.attendanceId = attendanceId;
        }

        public int getAttendanceId() {
            return this.attendanceId;
        }

        private void retrieveWhetherAttendanceWasTaken(int isTaken) {
            if (isTaken == ATTENDANCE_ALREADY_TAKEN) {
                this.isAttendanceTaken = true;
            } else if (isTaken == ATTENDANCE_NOT_TAKEN) {
                this.isAttendanceTaken = false;
            }
        }

        public boolean getWasAttendanceTaken() {
            return this.isAttendanceTaken;
        }

        private void retrieveEmployeeStoredAttendanceState(int attendanceState) {
            if (attendanceState == ATTENDANCE_STATE_PRESENT) {
                this.employeeStoredAttendanceState = Boolean.TRUE;
            } else if (attendanceState == ATTENDANCE_STATE_ABSENT) {
                this.employeeStoredAttendanceState = Boolean.FALSE;
            } else if (attendanceState == ATTENDANCE_STATE_NOT_SET) {
                employeeStoredAttendanceState = null;
            }
        }

        /**
         * Helper method for retrieveEmployeeStoredAttendanceState()
         *
         * @param state
         * @return <code>int</code>
         */
        private int retrieveAttendanceStoredStateFromResultSet(boolean state) {
            if (state) {
                return ATTENDANCE_STATE_PRESENT;
            } else {
                return ATTENDANCE_STATE_ABSENT;
            }
        }

        /**
         * Get the stored attendance state.
         *
         * The result is used to determine one of three cases: - There was no
         * state recorded, in this case the returned state is null. - There was
         * attendance state; which can be either true or false.
         *
         * Based on the state: If it is null that means no state was set
         * previously and we need to record the input state. If it is either
         * true or false; we compare the input state with the stored state. If
         * the input and stored state are the same value, then no need to
         * update. If the input and stored state are not the same value, then an
         * update is issued with the new input state replacing the previous
         * stored state.
         *
         * @return <code>Boolean</code> object.
         */
        public Boolean getEmployeeStoredAttendanceState() {
            return employeeStoredAttendanceState;
        }

        private void setCreatedOrFailed(int createdOrFailed) {
            if (createdOrFailed == 1) {
                this.created = ATTENDANCE_CREATED;
            } else {
                this.created = FAIL_ON_CREATE_OR_UPDATE;
            }
        }

        public boolean getCreateState() {
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

        public boolean getUpdateState() {
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
