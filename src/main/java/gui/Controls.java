/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.attendance.AttendanceTab;
import gui.performance.PerformanceTab;
import gui.salary.SalaryTab;
import gui.salaryadvance.SalaryAdvanceTab;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Saleh
 */
public class Controls extends JPanel {

    private JTabbedPane tabs;
    private AttendanceTab attendanceTab;
    private PerformanceTab performanceTab;
    private SalaryAdvanceTab salaryAdvanceTab;
    private SalaryTab salaryTab;

    public Controls() {
        super();
        this.setLayout(new GridLayout(1, 0));

        tabs = new JTabbedPane();

        attendanceTab = new AttendanceTab();
        tabs.add(attendanceTab, "Attendance");

        performanceTab = new PerformanceTab();
        tabs.add(performanceTab.performanceTab(), "Performance");

        salaryAdvanceTab = new SalaryAdvanceTab();
        tabs.add(salaryAdvanceTab.salaryAdvanceTab(), "Salary Advance");

        salaryTab = new SalaryTab();
        tabs.add(salaryTab.getContainer(), "Salary");

        this.add(tabs);
    }

    public AttendanceTab getAttendanceTab() {
        return this.attendanceTab;
    }

    public PerformanceTab getPerformanceTab() {
        return performanceTab;
    }

    public SalaryTab getSalaryTab() {
        return salaryTab;
    }

}
