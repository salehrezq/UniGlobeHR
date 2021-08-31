/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import datalink.CRUDAttendance;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import model.Attendance;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class EmployeeDailyAttendance extends JPanel implements DateListener, EmployeeSelectedListener {

    private JRadioButton btnSetPresent;
    private JRadioButton btnSetAbsent;
    private ButtonGroup btnGroup;
    private static DatePicker datePicker;
    private static AttendanceHandler attendanceHandler;
//    private static LocalDate dateAbsentSelected;
    private Employee employeeContext;
    private CRUDAttendance.EmployeeAttendanceStatus eas;

    public EmployeeDailyAttendance() {
        super();

        eas = new CRUDAttendance.EmployeeAttendanceStatus();

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
//        dateAbsentSelected = datePicker.getDate();
        datePicker.addDateListener(this);
//        datePicker.addDateListener(this);
        this.add(datePicker.getDatePicker());
    }

    public LocalDate getAbsentSelectedDate() {
        return datePicker.getDate();
    }

    @Override
    public void dateChanged(LocalDate date) {
        if (this.employeeContext != null) {

            eas = CRUDAttendance.getEmployeeAttendanceStatusOnSpecificDate(employeeContext.getId(), date);

            btnSetPresent.setEnabled(true);
            btnSetAbsent.setEnabled(true);

            if (eas.getWasAttendanceBeenTaken()) {
                if (eas.getEmployeeAttendanceState()) {
                    // present
                    btnSetPresent.setSelected(true);
                } else {
                    // absent
                    btnSetAbsent.setSelected(true);
                }
            } else {
                // No attendance taken
                btnGroup.clearSelection();
            }
        } else {
            btnSetPresent.setEnabled(false);
            btnSetAbsent.setEnabled(false);
            btnGroup.clearSelection();
        }
    }

    @Override
    public void employeeSelected(Employee employee) {
        employeeContext = employee;
        btnSetPresent.setEnabled(true);
        btnSetAbsent.setEnabled(true);

        eas = CRUDAttendance.getEmployeeAttendanceStatusOnSpecificDate(employeeContext.getId(), datePicker.getDate());

        if (eas.getWasAttendanceBeenTaken()) {
            if (eas.getEmployeeAttendanceState()) {
                // present
                btnSetPresent.setSelected(true);
            } else {
                // absent
                btnSetAbsent.setSelected(true);
            }
        } else {
            btnGroup.clearSelection();
        }
    }

    @Override
    public void employeeDeselected() {
        employeeContext = null;
        btnSetPresent.setEnabled(false);
        btnSetAbsent.setEnabled(false);
        btnGroup.clearSelection();
    }

    private class AttendanceHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {

            JRadioButton source = (JRadioButton) arg0.getSource();

            Attendance attendance = new Attendance();
            attendance.setEmployeeId(employeeContext.getId());
            attendance.setDate(datePicker.getDate());

            if (source == btnSetPresent) {
                attendance.setStateOfAttendance(true);
            } else if (source == btnSetAbsent) {
                attendance.setStateOfAttendance(false);
            }

            eas = CRUDAttendance.takeAttendance(attendance);
        }

    }

}
