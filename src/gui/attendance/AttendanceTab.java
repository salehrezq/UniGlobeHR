/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendance;

import gui.DatePicker;
import gui.EmployeeCard;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

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
    private EditAttendanceMode editAttendanceMode;
    private MonthelyAbsence monthelyAbsence;

    public AttendanceTab() {

        super();
        this.setBorder(BorderFactory.createLineBorder(Color.red));

        gridbag = new GridBagLayout();

        this.setLayout(gridbag);
        GridBagConstraints c;

        employeeCard = new EmployeeCard();
        c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(employeeCard, c);

        employeeDailyAttendance = new EmployeeDailyAttendance();
        c = new GridBagConstraints();
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(employeeDailyAttendance, c);

        employeeAttendLate = new EmployeeAttendLate();
        employeeDailyAttendance.addEmployeeAttendanceListener(employeeAttendLate);
        employeeDailyAttendance.addEmployeeAttendanceDataListener(employeeAttendLate);
        employeeDailyAttendance.addDateChangedAttendanceDataListener(employeeAttendLate);
        c = new GridBagConstraints();
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(employeeAttendLate, c);

        JPanel gatherSubmitEditPanel = new JPanel();

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
        gatherSubmitEditPanel.add(submitAttendancePanel);

        editAttendanceMode = new EditAttendanceMode();
        employeeDailyAttendance.addEmployeeAttendanceDataListener(editAttendanceMode);
        employeeDailyAttendance.addDateChangedAttendanceDataListener(editAttendanceMode);
        submitAttendancePanel.addSubmitAttendanceListener(editAttendanceMode);
        editAttendanceMode.addAttendanceEditModeListener(employeeDailyAttendance);
        editAttendanceMode.addAttendanceEditModeListener(employeeAttendLate);
        editAttendanceMode.addAttendanceEditModeListener(submitAttendancePanel);
        c = new GridBagConstraints();
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        gatherSubmitEditPanel.add(editAttendanceMode);

        this.add(gatherSubmitEditPanel, c);

        monthelyAbsence = new MonthelyAbsence();
        c = new GridBagConstraints();
        c.gridy = 4;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
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
