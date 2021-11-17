/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendance;

import datalink.CRUDAttendance;
import datalink.CRUDLateAttendance;
import gui.DateListener;
import gui.EmployeeSelectedListener;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import model.Attendance;
import model.Employee;
import model.Late;

/**
 *
 * @author Saleh
 */
public class SubmitAttendance extends JPanel
        implements
        EmployeeSelectedListener,
        EmployeeAttendanceListener,
        LateAttendanceListener,
        EmployeeAttendanceDataListener,
        DateChangedAttendanceDataListener,
        DateListener,
        AttendanceEditModeListener {

    private final JButton btnSubmitAttendance;
    private Attendance attendance;
    private Late lateAttendance;
    private CRUDAttendance.EmployeeAttendanceStatus eas;
    private LocalDate date;
    private ArrayList<SubmitAttendanceListener> submitAttendanceListeners;

    public SubmitAttendance() {
        btnSubmitAttendance = new JButton("Submit");
        btnSubmitAttendance.setEnabled(false);
        this.add(btnSubmitAttendance);
        btnSubmitAttendance.addActionListener(new SubmitAttendanceAction());
        submitAttendanceListeners = new ArrayList<>();
    }

    public void setDateInitial(LocalDate date) {
        this.date = date;
    }

    public void addSubmitAttendanceListener(SubmitAttendanceListener sal) {
        this.submitAttendanceListeners.add(sal);
    }

    private void notifyAttendanceSubmitSuccedded() {
        this.submitAttendanceListeners.forEach((sal) -> {
            sal.attendanceSubmitSucceeded();
        });
    }

    @Override
    public void employeeSelected(Employee employee) {
        btnSubmitAttendance.setEnabled(false);
        attendance = new Attendance();
        attendance.setEmployeeId(employee.getId());
        attendance.setDate(this.date);
    }

    @Override
    public void employeeDeselected() {
        btnSubmitAttendance.setEnabled(false);
        attendance = null;
    }

    @Override
    public void employeeIsPresent() {
        attendance.setStateOfAttendance(true);
        btnSubmitAttendance.setEnabled(true);
    }

    @Override
    public void employeeIsAbsent() {
        attendance.setStateOfAttendance(false);
        btnSubmitAttendance.setEnabled(true);
    }

    @Override
    public void employeeAttendanceDataOnSelection(CRUDAttendance.EmployeeAttendanceStatus eas) {
        btnSubmitAttendance.setEnabled(false);
    }

    @Override
    public void employeeSelectionCleared() {
        btnSubmitAttendance.setEnabled(false);
    }

    /**
     * In case date changed and employee is already recorded in the attendance.
     *
     * @param eas
     */
    @Override
    public void dateChanged(CRUDAttendance.EmployeeAttendanceStatus eas) {
        btnSubmitAttendance.setEnabled(false);
    }

    /**
     * In case date changed but employee is not recorded in the attendance.
     *
     * @param date
     */
    @Override
    public void dateChanged(LocalDate date) {
        this.date = date;
        if (attendance != null) {
            attendance.setDate(date);
        }
    }

    @Override
    public void employeeAttendedLate() {
        lateAttendance = new Late();
        // default one minute
        lateAttendance.setMinutes_late(1);
        btnSubmitAttendance.setEnabled(true);
    }

    @Override
    public void attendMinutesLate(int minutesLate) {
        if (lateAttendance != null) {
            lateAttendance.setMinutes_late(minutesLate);
            btnSubmitAttendance.setEnabled(true);
        }
    }

    @Override
    public void employeeAttendedFine() {
        lateAttendance = null;
        btnSubmitAttendance.setEnabled(true);
    }

    @Override
    public void attendanceEditModeReact(Attendance attendance, Late lateAttendance) {
        this.attendance = attendance;
        this.lateAttendance = lateAttendance;
    }

    class SubmitAttendanceAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent arg0) {

            // Once clicked; set submit button to false.
            btnSubmitAttendance.setEnabled(false);

            // Uset to flag atomic commit to the database.
            boolean commitAndDontWaitAnotherExecuteStmt = false;

            // true if employee is either absent or present without being late.
            // If employee is present and late then the value become false.
            // false flag is used to allow late attendance record.
            boolean employeeIsEitherAbsentOrAttendedFine
                    = (attendance.getStateOfAttendance() == false)
                    || (attendance.getStateOfAttendance() && lateAttendance == null);

            // true if employee is present and late.
            boolean employeeAttendedAndLate
                    = attendance.getStateOfAttendance()
                    && lateAttendance != null;

            // if either employee is absent or present without being late
            // flag set to true to not wait for late record;
            // because there is no late record.
            if (employeeIsEitherAbsentOrAttendedFine) {
                commitAndDontWaitAnotherExecuteStmt = true;
            }

            // If employee is present and also late then flag set to false
            // to not commit immediately but wait for late record to be inserted
            // and after that; commit both the attendance and late record in a single commit.
            if (employeeAttendedAndLate) {
                commitAndDontWaitAnotherExecuteStmt = false;
            }

            // Either create or update attendance record.
            // Or if the passed attendance is the same as the stored record
            // then no update is issued. However, below the code you will find
            // the case of attendance state is present and it is the same as the stored
            // but late record is updated.
            eas = CRUDAttendance.takeAttendance(attendance, commitAndDontWaitAnotherExecuteStmt);

            //  ******************************************************************************
            //  At this point; the attendance is either created or updated or nothing changed.
            //  ******************************************************************************
            //**
            // if either employee is absent or present without being late
            // and there was creat or update operation, then;
            // notify attendance submition succedded.
            if (employeeIsEitherAbsentOrAttendedFine) {
                if (eas.getCreateState() || eas.getUpdateState()) {
                    notifyAttendanceSubmitSuccedded();
                }
            }

            int lateAttendanceInserted = 0;
            // The attendance is already as above either created or updated
            // and the state of attendance is present and employee is also late
            if (((eas.getCreateState() || eas.getUpdateState())
                    && attendance.getStateOfAttendance() && lateAttendance != null)) {
                // Set attendance id for the late record
                lateAttendance.setAttendance_id(eas.getAttendanceId());
                // Insert the late attendance data.
                lateAttendanceInserted = CRUDLateAttendance.create(lateAttendance, true);

                if (lateAttendanceInserted == 1) {
                    notifyAttendanceSubmitSuccedded();
                }
            }

            boolean lateRecordAreadyExist = false;
            Late lateAttendanceStored = CRUDLateAttendance.getByAttendanceId(attendance.getId());
            // Following code is to affect only late record
            // either create or update or delete.
            if (!eas.getCreateState() && !eas.getUpdateState()
                    && (attendance.getStateOfAttendance() && lateAttendance != null)) {
                // Case of late checkbox checked; while the state was already present without change.
                // Set attendance id for the late record
                lateAttendance.setAttendance_id(eas.getAttendanceId());
                // Check if late record is not already exist.
                // If late record is not exist then it is create operation.
                if (lateAttendanceStored == null) {
                    // Insert the late attendance data.
                    lateAttendanceInserted = CRUDLateAttendance.create(lateAttendance, false);
                    if (lateAttendanceInserted == 1) {
                        notifyAttendanceSubmitSuccedded();
                    }
                } else {
                    // late record is exist then it is update operation.
                    // Update only if passed late entity minutes are different from stored late entity.
                    lateRecordAreadyExist = true;
                    if (lateAttendance.getMinutes_late() != lateAttendanceStored.getMinutes_late()) {
                        CRUDLateAttendance.update(lateAttendance, false);
                    }
                    notifyAttendanceSubmitSuccedded();
                }
            } else if (!eas.getCreateState() && !eas.getUpdateState()
                    && (attendance.getStateOfAttendance() && lateAttendance == null)) {
                // Case of late checkbox unchecked; while the stored state is already present without change.
                // then it is delete operation.
                int deleteLateAttendance = CRUDLateAttendance.deleteByAttendenceId(attendance.getId());
                if (deleteLateAttendance == 1) {
                    notifyAttendanceSubmitSuccedded();
                }
            }

            if ((eas.getCreateState() == false)
                    && (eas.getUpdateState() == false)
                    && ((lateAttendance != null) && (lateAttendanceInserted == 0) && !lateRecordAreadyExist)) {
                btnSubmitAttendance.setEnabled(true);
            }
        }
    }
}
