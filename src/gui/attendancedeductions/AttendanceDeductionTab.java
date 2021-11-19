/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendancedeductions;

import gui.EmployeeCard;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

/**
 *
 * @author Saleh
 */
public class AttendanceDeductionTab extends JPanel {

    private final GridBagLayout gridbag;
    private EmployeeCard employeeCard;
    private MonthAttendanceDeductions monthAttendanceDeductions;

    public AttendanceDeductionTab() {
        super();
        this.gridbag = new GridBagLayout();
        this.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();

        employeeCard = new EmployeeCard();
        c = new GridBagConstraints();
        c.gridy = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;
        this.add(employeeCard, c);

        monthAttendanceDeductions = new MonthAttendanceDeductions();
        c = new GridBagConstraints();
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;
        this.add(monthAttendanceDeductions.getPanelTable(), c);
    }

    public EmployeeCard geteEmployeeCard() {
        return this.employeeCard;
    }

    public MonthAttendanceDeductions getMonthAttendanceDeductions() {
        return monthAttendanceDeductions;
    }

}
