/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

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
public class ManageEmployee extends JPanel {

    private final GridBagLayout gridbag;

    private EmployeeCard employeeCard;
    private EmployeeDailyAttendance employeeDailyAttendance;
    private EmployeeAttendLate employeeAttendLate;
    private SubmitAttendance submitAttendancePanel;
    private MonthelyAbsence monthelyAbsence;

    public ManageEmployee() {

        super();
        this.setBorder(BorderFactory.createLineBorder(Color.red));

        gridbag = new GridBagLayout();

        this.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();

        employeeCard = new EmployeeCard();

        c = new GridBagConstraints();
        c.gridy = 1;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(employeeCard, c);

        employeeDailyAttendance = new EmployeeDailyAttendance();

        c = new GridBagConstraints();
        c.gridy = 2;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(employeeDailyAttendance, c);

        employeeAttendLate = new EmployeeAttendLate();
        employeeDailyAttendance.addEmployeeAttendanceListener(employeeAttendLate);
        employeeDailyAttendance.addEmployeeAttendanceDataListener(employeeAttendLate);
        employeeDailyAttendance.addEmployeeAttendanceDataListener(employeeAttendLate);
        c = new GridBagConstraints();
        c.gridy = 3;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(employeeAttendLate, c);

        submitAttendancePanel = new SubmitAttendance();
        employeeDailyAttendance.addEmployeeAttendanceListener(submitAttendancePanel);
        c = new GridBagConstraints();
        c.gridy = 4;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(submitAttendancePanel, c);

        monthelyAbsence = new MonthelyAbsence();

        c = new GridBagConstraints();
        c.gridy = 5;
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
