/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendance;

import datalink.CRUDAttendance;
import gui.DateListener;
import gui.DatePicker;
import gui.EmployeeSelectedListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import model.Attendance;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class EmployeeDailyAttendance extends JPanel
        implements
        DateListener,
        EmployeeSelectedListener,
        SubmitAttendanceListener {

    private JRadioButton btnSetPresent;
    private JRadioButton btnSetAbsent;
    private final boolean boolPresent = true;
    private final boolean boolAbsent = false;
    private ButtonGroup btnGroup;
    private DatePicker datePicker;
    private static AttendanceHandler attendanceHandler;
    private Employee employeeContext;
    private CRUDAttendance.EmployeeAttendanceStatus eas;
    private ArrayList<EmployeeAttendanceListener> employeeAttendanceListeners;
    private ArrayList<EmployeeAttendanceDataListener> employeeAttendanceData;
    private ArrayList<DateChangedAttendanceDataListener> dateChangedAttendanceDatas;

    public EmployeeDailyAttendance() {
        super();

        eas = new CRUDAttendance.EmployeeAttendanceStatus();
        employeeAttendanceListeners = new ArrayList<>();
        employeeAttendanceData = new ArrayList<>();
        dateChangedAttendanceDatas = new ArrayList<>();

        btnSetPresent = new JRadioButton("Present");
        btnSetAbsent = new JRadioButton("Absent");

        btnSetPresent.setEnabled(false);
        btnSetAbsent.setEnabled(false);

        btnGroup = new ButtonGroup();
        btnGroup.add(btnSetPresent);
        btnGroup.add(btnSetAbsent);

        attendanceHandler = new AttendanceHandler();

        btnSetPresent.addActionListener(attendanceHandler);
        btnSetAbsent.addActionListener(attendanceHandler);

        datePicker = new DatePicker();

        this.add(btnSetPresent);
        this.add(btnSetAbsent);

        // initial date value settings
        datePicker.setTodayAsDefault();
        datePicker.addDateListener(this);
        this.add(datePicker.getDatePicker());
    }

    public DatePicker getDatePicker() {
        return this.datePicker;
    }

    public LocalDate getAbsentSelectedDate() {
        return datePicker.getDate();
    }

    @Override
    public void dateChanged(LocalDate date) {
        if (this.employeeContext != null) {
            // Employee node is selected, then check the state of attendance and
            // initialize the interface accordingly
            eas = CRUDAttendance.getEmployeeAttendanceStatusOnSpecificDate(employeeContext.getId(), date);
            notifyDateChanged(eas);

            if (eas.getWasAttendanceTaken()) {
                btnSetPresent.setEnabled(false);
                btnSetAbsent.setEnabled(false);
                // Attendance was taken, then initialize
                // the buttons to which state; present or absent
                if (eas.getEmployeeStoredAttendanceState()) {
                    // The state was present
                    btnSetPresent.setSelected(true);
                } else {
                    // The state was absent
                    btnSetAbsent.setSelected(true);
                }
            } else {
                // Attendance was NOT taken yet, then clear the buttons
                btnGroup.clearSelection();
                btnSetPresent.setEnabled(true);
                btnSetAbsent.setEnabled(true);
            }
        } else {
            // No employee node is selected, so disable attendance buttons
            // and clear the selection
            btnSetPresent.setEnabled(false);
            btnSetAbsent.setEnabled(false);
            btnGroup.clearSelection();
        }
    }

    @Override
    public void employeeSelected(Employee employee) {
        employeeContext = employee;

        eas = CRUDAttendance.getEmployeeAttendanceStatusOnSpecificDate(employeeContext.getId(), datePicker.getDate());
        notifyEmployeeAttendanceDataOnSelection(eas);

        if (eas.getWasAttendanceTaken()) {
            btnSetPresent.setEnabled(false);
            btnSetAbsent.setEnabled(false);
            // Attendance was taken, then initialize
            // the buttons which state; present or absent
            if (eas.getEmployeeStoredAttendanceState()) {
                // The state was present
                btnSetPresent.setSelected(true);
            } else {
                // The state was absent
                btnSetAbsent.setSelected(true);
            }
        } else {
            // Attendance was NOT taken yet, then clear the buttons
            btnGroup.clearSelection();
            btnSetPresent.setEnabled(true);
            btnSetAbsent.setEnabled(true);
        }
    }

    @Override
    public void employeeDeselected() {
        btnSetPresent.setEnabled(false);
        btnSetAbsent.setEnabled(false);
        employeeContext = null;
        btnGroup.clearSelection();
        notifyEmployeeSelectionCleared();
    }

    @Override
    public void attendanceSubmitSucceeded() {
        this.btnSetPresent.setEnabled(false);
        this.btnSetAbsent.setEnabled(false);
    }

    @Override
    public void attendanceSubmitFailed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class AttendanceHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {

            JRadioButton source = (JRadioButton) arg0.getSource();

            Attendance attendance = new Attendance();
            attendance.setEmployeeId(employeeContext.getId());
            attendance.setDate(datePicker.getDate());

            if (source == btnSetPresent) {
                attendance.setStateOfAttendance(boolPresent);
                notifyEmployeePresence();
            } else if (source == btnSetAbsent) {
                attendance.setStateOfAttendance(boolAbsent);
                notifyEmployeeAbsence();
            }

            // eas = CRUDAttendance.takeAttendance(attendance);
        }
    }

    public void addEmployeeAttendanceListener(EmployeeAttendanceListener eal) {
        this.employeeAttendanceListeners.add(eal);
    }

    private void notifyEmployeePresence() {
        this.employeeAttendanceListeners.forEach((eal) -> {
            eal.employeeIsPresent();
        });
    }

    private void notifyEmployeeAbsence() {
        this.employeeAttendanceListeners.forEach((eal) -> {
            eal.employeeIsAbsent();
        });
    }

    public void addEmployeeAttendanceDataListener(EmployeeAttendanceDataListener ead) {
        this.employeeAttendanceData.add(ead);
    }

    private void notifyEmployeeAttendanceDataOnSelection(CRUDAttendance.EmployeeAttendanceStatus eas) {
        this.employeeAttendanceData.forEach((ead) -> {
            ead.employeeAttendanceDataOnSelection(eas);
        });
    }

    private void notifyEmployeeSelectionCleared() {
        this.employeeAttendanceData.forEach((ead) -> {
            ead.employeeSelectionCleared();
        });
    }

    public void addDateChangedAttendanceDataListener(DateChangedAttendanceDataListener dcad) {
        this.dateChangedAttendanceDatas.add(dcad);
    }

    private void notifyDateChanged(CRUDAttendance.EmployeeAttendanceStatus eas) {
        this.dateChangedAttendanceDatas.forEach((dcad) -> {
            dcad.dateChanged(eas);
        });
    }

}
