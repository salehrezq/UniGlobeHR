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
        DateListener {

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
    }

    @Override
    public void attendMinutesLate(int minutesLate) {
        if (lateAttendance != null) {
            lateAttendance.setMinutes_late(minutesLate);
        }
    }

    @Override
    public void employeeAttendedFine() {
        lateAttendance = null;
    }

    class SubmitAttendanceAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            btnSubmitAttendance.setEnabled(false);

            boolean commitAndDontWaitAnotherExecuteStmt = false;

            boolean employeeIsEitherAbsentOrAttendedFine
                    = (attendance.getStateOfAttendance() == false)
                    || (attendance.getStateOfAttendance() && lateAttendance == null);

            boolean employeeAttendedAndLate
                    = attendance.getStateOfAttendance()
                    && lateAttendance != null;

            if (employeeIsEitherAbsentOrAttendedFine) {
                commitAndDontWaitAnotherExecuteStmt = true;
            }

            if (employeeAttendedAndLate) {
                commitAndDontWaitAnotherExecuteStmt = false;
            }

            eas = CRUDAttendance.takeAttendance(attendance, commitAndDontWaitAnotherExecuteStmt);

            if (employeeIsEitherAbsentOrAttendedFine) {
                if (eas.getCreateState()) {
                    notifyAttendanceSubmitSuccedded();
                }
            }

            int lateAttendanceInserted = 0;
            // Employee attended but late.
            if (eas.getCreateState() && attendance.getStateOfAttendance() && lateAttendance != null) {
                // Set attendance id for the late record 
                lateAttendance.setAttendance_id(eas.getAttendanceId());
                // Insert the late attendance data.
                lateAttendanceInserted = CRUDLateAttendance.create(lateAttendance);

                if (lateAttendanceInserted == 1) {
                    notifyAttendanceSubmitSuccedded();
                }
            }

            if (eas.getCreateState() == false || ((lateAttendance != null) && (lateAttendanceInserted == 0))) {
                btnSubmitAttendance.setEnabled(true);
            }
        }
    }
}
