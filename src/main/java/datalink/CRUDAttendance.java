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
import model.AbsentOrLateEntity;
import model.Attendance;
import model.Late;

/**
 *
 * @author Saleh
 */
public class CRUDAttendance {

    private static Connection conn;
    private static boolean commitAndDontWaitAnotherExecuteStmt;

    /**
     * Inserts <code>Attendance</code> entity to the database, and returns the
     * state of creation.
     *
     * @param attendance entity to be stored into the database
     * @param eas that will be updated with the create state of attendance
     * @param commitAndDontWaitAnotherExecuteStmt if {@code false} then employee
     * is present and late. In this case, the INSERT statement will not be
     * committed here. However the same connection will be used on the late
     * entity operation and there will be committed, so both attendance and late
     * will be committed altogether or fail altogether. If {@code true} then no
     * follow up related late entity, so the INSERT will be committed here.
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

            eas.setCreatedOrFailed(create == 1);
            if (eas.getCreateState() == false) {
                // Failed to insert attendance record.
                JOptionPane.showConfirmDialog(null,
                        "Fail to create attendance", "",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }

            if (eas.getCreateState()) {
                // Succeeded to insert attendance record, now get the record id.
                try (ResultSet generatedKeys = p.getGeneratedKeys()) {
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
     * Updates {@code Attendance} entity in the database, and returns the state
     * of updating.
     *
     * @param attendance entity to be used to update relevant record in the
     * database.
     * @param eas this will be updated with the update state of attendance.
     * @param commitAndDontWaitAnotherExecuteStmt if {@code false} then employee
     * is present and late. In this case, the UPDATE statement will not be
     * committed here. However the same connection will be used on the late
     * entity operation and there will be committed, so both attendance and late
     * will be committed altogether or fail altogether. If {@code true} then no
     * follow up related late entity, so the UPDATE will be committed here.
     * @return <code>EmployeeAttendanceStatus</code> that contains the update
     * state of attendance.
     */
    private static EmployeeAttendanceStatus update(Attendance attendance, EmployeeAttendanceStatus eas, boolean commitAndDontWaitAnotherExecuteStmt) {

        int update = 0;

        try {
            String sql = "UPDATE `attendance` SET `state`= ? WHERE (`employee_id` = ? AND `date` = ?)";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setBoolean(1, attendance.getStateOfAttendance());
            p.setInt(2, attendance.getEmployeeId());
            p.setObject(3, attendance.getDate());
            update = p.executeUpdate();
            eas.setUpdatedOrFailed(update == 1);

            if (commitAndDontWaitAnotherExecuteStmt && eas.getUpdateState()) {
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
     * Update the {@code locked } attribute of the relation to {@code true }
     * if salary is paid or to {@code false } if salary is deleted. This method
     * uses the passed connection to execute update on it to benefit from atomic
     * commit for data consistency sake.
     *
     * @param conn
     * @param employeeId
     * @param yearMonthSubject
     * @throws SQLException
     */
    private static void switchRelatedAttendanceLock(Connection conn, int employeeId, LocalDate yearMonthSubject, boolean locked) throws SQLException {
        String sqlLockSwitchAttendance = "UPDATE `attendance` SET `locked` = " + locked + " "
                + "WHERE `employee_id` = ? AND `date` >= ? AND `date` < ?";

        LocalDate firstOfThisMonth = H.getYearMonth(yearMonthSubject).atDay(1);
        LocalDate firstOfNextMonth = H.getYearMonth(yearMonthSubject).plusMonths(1).atDay(1);

        PreparedStatement locSwitchkAttendance = conn.prepareStatement(sqlLockSwitchAttendance);
        locSwitchkAttendance.setInt(1, employeeId);
        locSwitchkAttendance.setObject(2, firstOfThisMonth);
        locSwitchkAttendance.setObject(3, firstOfNextMonth);
        locSwitchkAttendance.executeUpdate();
    }

    protected static void lockRelatedAttendance(Connection conn, int employeeId, LocalDate yearMonthSubject) throws SQLException {
        switchRelatedAttendanceLock(conn, employeeId, yearMonthSubject, true);
    }

    protected static void unlockRelatedAttendance(Connection conn, int employeeId, LocalDate yearMonthSubject) throws SQLException {
        switchRelatedAttendanceLock(conn, employeeId, yearMonthSubject, false);
    }

    /**
     * Takes the attendance using either create or update, if input state of
     * attendance is the same as the stores, then no update is needed.
     *
     * @param attendance that will feed the method with the attendance state of
     * employee
     * @param commitAndDontWaitAnotherExecuteStmt since auto commit is set to
     * <code>false</code> for better control; so that we can achieve atomic
     * operation <code>false</code>. It will be set to <code>true</code> in
     * normal cases, and <code>false</code> when we need to execute more than
     * one insert statement which have to be either all inserted altogether
     * successfully or fail altogether. For example the case when we need to
     * insert both attendance as present and also insert a `late` record for
     * that attendance.
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
                // check if stored and passed attendance are both the same state
                // no matter it is present or absent.
                // No need to update attendance since the input the same as the stored value.
                // then check if Late data changed instead.
                // flag eas.setWhetherUpdateNeeded(false);
                eas.setWhetherUpdateNeeded(false);
            } else {
                // input is different from the stored value, so we need to update it.
                update(attendance, eas, commitAndDontWaitAnotherExecuteStmt);
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
            String sql = "SELECT `id`, `state`, `locked` FROM `attendance` WHERE `employee_id` = ? AND `date` = ?";
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
                eas.retrieveWhetherAttendanceWasTaken(true);
                eas.retrieveEmployeeStoredAttendanceState(result.getBoolean("state"));
                eas.retrieveAttendanceStoredLockState(result.getBoolean("locked"));
            } else {
                eas.retrieveWhetherAttendanceWasTaken(false);
                eas.retrieveEmployeeStoredAttendanceState(null);
                eas.retrieveAttendanceStoredLockState(null);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }

        return eas;
    }

    public static List getAbsenceRecordByEmployeeByMonth(int employeeID, YearMonth ym) {

        List<Attendance> absentRecordList = new ArrayList<>();

        try {

            //  YearMonth yearMonth = YearMonth.of(ym.getYear(), ym.getMonthValue());
            LocalDate firstOfThisMonth = ym.atDay(1);
            LocalDate firstOfNextMonth = ym.plusMonths(1).atDay(1);

            String sql = "SELECT `id`, `date` FROM `attendance` WHERE `employee_id` = ? AND `date` >= ? AND `date` < ? AND `state` = ?  ORDER BY `date` ASC";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, employeeID);
            p.setObject(2, firstOfThisMonth);
            p.setObject(3, firstOfNextMonth);
            p.setBoolean(4, false);

            ResultSet result = p.executeQuery();

            Attendance absentRecord = null;

            while (result.next()) {
                absentRecord = new Attendance();
                absentRecord.setId(result.getInt("id"));
                absentRecord.setDate(result.getDate("date").toLocalDate());
                absentRecord.setStateOfAttendance(false);
                absentRecord.setEmployeeId(employeeID);
                absentRecordList.add(absentRecord);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return absentRecordList;
    }

    public static List getAbsenceAndLatesRecordByEmployeeByMonth(int employeeID, YearMonth ym) {

        List<AbsentOrLateEntity> absentOrLateEnities = new ArrayList<>();

        try {
            LocalDate firstOfThisMonth = ym.atDay(1);
            LocalDate firstOfNextMonth = ym.plusMonths(1).atDay(1);

            String sql = "SELECT *"
                    + " FROM `attendance` LEFT JOIN `late` ON (attendance.id = late.attendance_id)"
                    + " WHERE attendance.employee_id = ?"
                    + " AND attendance.date >= ? AND attendance.date < ?"
                    + " AND (attendance.state = FALSE OR (attendance.state = TRUE AND late.id IS NOT NULL))"
                    + " ORDER BY `date` ASC;";

            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);

            p.setInt(1, employeeID);
            p.setObject(2, firstOfThisMonth);
            p.setObject(3, firstOfNextMonth);

            ResultSet result = p.executeQuery();

            Attendance absentRecord = null;
            Late lateAttendance = null;

            while (result.next()) {

                Integer lateID = (Integer) result.getObject("late.id");

                if (lateID == null) {
                    // Case of employee is absent
                    absentRecord = new Attendance();
                    absentRecord.setId(result.getInt("attendance.id"));
                    absentRecord.setDate(result.getDate("attendance.date").toLocalDate());
                    absentRecord.setStateOfAttendance(false);
                    absentRecord.setEmployeeId(employeeID);
                    absentOrLateEnities.add(absentRecord);
                } else {
                    // Case of employee is late
                    lateAttendance = new Late();
                    lateAttendance.setId(lateID);
                    lateAttendance.setEmployeeId(employeeID);
                    lateAttendance.setDate(result.getDate("attendance.date").toLocalDate());
                    lateAttendance.setAttendance_id(result.getInt("late.attendance_id"));
                    lateAttendance.setMinutes_late(result.getInt("late.minutes_late"));
                    absentOrLateEnities.add(lateAttendance);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return absentOrLateEnities;
    }

    public static Attendance getById(int id) {

        Attendance attendance = null;

        try {

            String sql = "SELECT * FROM `attendance` WHERE `id` = ?";
            conn = Connect.getConnection();
            PreparedStatement p = conn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet result = p.executeQuery();

            while (result.next()) {
                attendance = new Attendance();
                attendance.setId(result.getInt("id"));
                attendance.setEmployeeId(result.getInt("employee_id"));
                attendance.setDate(result.getDate("date").toLocalDate());
                attendance.setStateOfAttendance(result.getBoolean("state"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CRUDAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Connect.cleanUp();
        }
        return attendance;
    }

    public static class EmployeeAttendanceStatus {

        private int attendanceId;
        private boolean isAttendanceTaken;
        private Boolean employeeStoredAttendanceState;
        private boolean created;
        private boolean updated;
        private boolean isUpdateNeeded;
        private Boolean locked;

        public void setAttendanceId(int attendanceId) {
            this.attendanceId = attendanceId;
        }

        public int getAttendanceId() {
            return this.attendanceId;
        }

        private void retrieveWhetherAttendanceWasTaken(boolean isTaken) {
            this.isAttendanceTaken = isTaken;
        }

        public boolean getWasAttendanceTaken() {
            return this.isAttendanceTaken;
        }

        private void retrieveEmployeeStoredAttendanceState(Boolean attendanceState) {
            this.employeeStoredAttendanceState = attendanceState;
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

        private void setCreatedOrFailed(boolean createdOrFailed) {
            this.created = createdOrFailed;
        }

        public boolean getCreateState() {
            return this.created;
        }

        private void setUpdatedOrFailed(boolean updatedOrFailed) {
            this.updated = updatedOrFailed;
        }

        public boolean getUpdateState() {
            return this.updated;
        }

        public boolean getWhetherUpdateNeeded() {
            return isUpdateNeeded;
        }

        private void setWhetherUpdateNeeded(boolean isUpdateNeeded) {
            this.isUpdateNeeded = isUpdateNeeded;
        }

        private void retrieveAttendanceStoredLockState(Boolean lock) {
            this.locked = lock;
        }

        private Boolean getAttendanceLockState() {
            return this.locked;
        }
    }

}
