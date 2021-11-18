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

    private void notifyAttendanceNoChange() {
        this.submitAttendanceListeners.forEach((sal) -> {
            sal.attendanceNoChange();
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
        lateAttendance = null;
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

            // Some entity state change occured
            boolean entityStatechanged = false;

            // Once clicked; set submit button to false.
            btnSubmitAttendance.setEnabled(false);

            // Used to flag atomic commit to the database.
            // If switched to false it enforces atomic commit
            // for both attendance with state present, and late.
            boolean commitAndDontWaitAnotherExecuteStmt = false;

            // true if employee is either absent or present without being late.
            // If employee is present and late then the value become false.
            boolean employeeIsEitherAbsentOrAttendedFine
                    = (attendance.getStateOfAttendance() == false)
                    || (attendance.getStateOfAttendance() && lateAttendance == null);

            // true if employee is present and late.
            boolean employeeAttendedAndLate
                    = attendance.getStateOfAttendance()
                    && lateAttendance != null;

            // if either employee is absent or present without being late
            // flag switched to true to not wait for late record;
            // because there is no late record.
            if (employeeIsEitherAbsentOrAttendedFine) {
                commitAndDontWaitAnotherExecuteStmt = true;
            }

            // If employee is present and also late then flag switched to false
            // to not commit immediately but wait for late record to be inserted
            // and after that; commit both the attendance and late record in a single commit.
            if (employeeAttendedAndLate) {
                commitAndDontWaitAnotherExecuteStmt = false;
            }

            // Either create or update attendance record.
            // Or if the passed attendance is the same as the stored record
            // then no update operation is commited. Note that Late entity operation is not
            // covered in this method, but subsequent code does.
            eas = CRUDAttendance.takeAttendance(attendance, commitAndDontWaitAnotherExecuteStmt);

            //  ******************************************************************************
            //  At this point; the attendance is either created or updated or nothing changed.
            //  ******************************************************************************
            //**
            // if either employee is absent or present without being late
            // and there was creat or update operation;
            // then switch entityStatechanged flag to true.
            if (employeeIsEitherAbsentOrAttendedFine) {
                if (eas.getCreateState() || eas.getUpdateState()) {
                    // Case where attendance stored state was present
                    // and now switched to absent, then delete any related late entity if any.
                    if (eas.getEmployeeStoredAttendanceState() && !attendance.getStateOfAttendance()) {
                        // if attendance state switched/updated from present to absent;
                        // remove related late record if any.
                        CRUDLateAttendance.deleteByAttendenceId(attendance.getId());
                    }
                    entityStatechanged = true;
                }
            }

            // Commiting the case of attendance with state present, and late;
            // is dependent on the completion of the late record. Also the
            // same thing for notify attendance submition succedded
            // which is achieved in the following codes
            int lateAttendanceInserted = 0;
            // The attendance is already as above either created or updated
            // and the state of attendance is present and employee is also late
            if (((eas.getCreateState() || eas.getUpdateState()) && employeeAttendedAndLate)) {
                // Set attendance id for the late record
                lateAttendance.setAttendance_id(eas.getAttendanceId());
                // Insert the late attendance data.
                lateAttendanceInserted = CRUDLateAttendance.create(lateAttendance, true);
                if (lateAttendanceInserted == 1) {
                    entityStatechanged = true;
                }
            }

            // Following codes is for when attendance not changed
            // and the state of attendance is present;
            // the only changes are related only to late entity operations
            // either create or update or delete.
            boolean lateRecordAreadyExist = false;
            Late lateAttendanceStored = CRUDLateAttendance.getByAttendanceId(attendance.getId());

            if (!eas.getCreateState() && !eas.getUpdateState() && attendance.getStateOfAttendance()) {
                if (lateAttendance != null) {
                    // Case of late checkbox checked; while the state was already present without change.
                    // Set attendance id for the late record
                    lateAttendance.setAttendance_id(eas.getAttendanceId());
                    // Check if late record is not already exist in the database.
                    // If late record is not exist then it is create operation.
                    if (lateAttendanceStored == null) {
                        // Insert the late attendance data.
                        lateAttendanceInserted = CRUDLateAttendance.create(lateAttendance, false);
                        if (lateAttendanceInserted == 1) {
                            entityStatechanged = true;
                        }
                    } else {
                        // late record is exist then it is update operation.
                        // Update only if passed late entity minutes are different from stored late entity.
                        lateRecordAreadyExist = true;
                        if (lateAttendance.getMinutes_late() != lateAttendanceStored.getMinutes_late()) {
                            CRUDLateAttendance.update(lateAttendance, false);
                            entityStatechanged = true;
                        }
                    }
                } else if (lateAttendance == null) {
                    // Case of late checkbox unchecked; while the stored state is already present without change.
                    // then it is delete operation.
                    int deleteLateAttendance = CRUDLateAttendance.deleteByAttendenceId(attendance.getId());
                    if (deleteLateAttendance == 1) {
                        entityStatechanged = true;
                    }
                }
            }

            if (entityStatechanged) {
                notifyAttendanceSubmitSuccedded();
            } else {
                notifyAttendanceNoChange();
            }

            if ((eas.getCreateState() == false)
                    && (eas.getUpdateState() == false)
                    && ((lateAttendance != null) && (lateAttendanceInserted == 0) && !lateRecordAreadyExist)) {
                btnSubmitAttendance.setEnabled(true);
            }
        }
    }
}
