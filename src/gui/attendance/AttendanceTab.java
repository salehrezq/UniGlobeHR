/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendance;

import gui.DatePicker;
import gui.EmployeeCard;
import gui.attendance.EmployeeAttendLate;
import gui.attendance.MonthelyAbsence;
import gui.attendance.EmployeeDailyAttendance;
import gui.attendance.SubmitAttendance;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class AttendanceTab extends JPanel {

    private final GridBagLayout gridbag;

    private EmployeeCard employeeCard;
    private EmployeeDailyAttendance employeeDailyAttendance;
    private EmployeeAttendLate employeeAttendLate;
    private SubmitAttendance submitAttendancePanel;
    private EditAttendance editAttendance;
    private MonthelyAbsence monthelyAbsence;

    public AttendanceTab() {

        super();
        this.setBorder(BorderFactory.createLineBorder(Color.red));

        gridbag = new GridBagLayout();

        this.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();

        employeeCard = new EmployeeCard();

        c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(employeeCard, c);

        employeeDailyAttendance = new EmployeeDailyAttendance();

        c = new GridBagConstraints();
        c.gridy = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(employeeDailyAttendance, c);

        employeeAttendLate = new EmployeeAttendLate();
        employeeDailyAttendance.addEmployeeAttendanceListener(employeeAttendLate);
        employeeDailyAttendance.addEmployeeAttendanceDataListener(employeeAttendLate);
        employeeDailyAttendance.addDateChangedAttendanceDataListener(employeeAttendLate);
        c = new GridBagConstraints();
        c.gridy = 2;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(employeeAttendLate, c);

        submitAttendancePanel = new SubmitAttendance();
        submitAttendancePanel.addSubmitAttendanceListener(employeeDailyAttendance);
        submitAttendancePanel.addSubmitAttendanceListener(employeeAttendLate);
        employeeDailyAttendance.addEmployeeAttendanceListener(submitAttendancePanel);
        employeeDailyAttendance.addEmployeeAttendanceDataListener(submitAttendancePanel);
        employeeDailyAttendance.addDateChangedAttendanceDataListener(submitAttendancePanel);
        employeeAttendLate.addLateAttendanceListener(submitAttendancePanel);
        DatePicker datePicker = employeeDailyAttendance.getDatePicker();
        submitAttendancePanel.setDateInitial(datePicker.getDefaultToday());
        datePicker.addDateListener(submitAttendancePanel);
        c = new GridBagConstraints();
        c.gridy = 3;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(submitAttendancePanel, c);
        
        editAttendance = new EditAttendance();
        c = new GridBagConstraints();
        c.gridy = 3;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(editAttendance, c);

        monthelyAbsence = new MonthelyAbsence();
        c = new GridBagConstraints();
        c.gridy = 4;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(monthelyAbsence.getPanelTable(), c);
    }

    public EmployeeCard getEmployeeCard() {
        return this.employeeCard;
    }

    public EmployeeDailyAttendance getEmployeeDailyAbsence() {
        return this.employeeDailyAttendance;
    }

    public SubmitAttendance getSubmitAttendancePanel() {
        return this.submitAttendancePanel;
    }

    public MonthelyAbsence getMonthelyAbsence() {
        return this.monthelyAbsence;
    }
}
