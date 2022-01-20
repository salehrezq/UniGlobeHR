/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.attendance.AttendanceTab;
import gui.performance.PerformanceTab;
import gui.salary.SalaryTab;
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
    private SalaryTab salaryTab;

    public Controls() {
        super();
        this.setLayout(new GridLayout(1, 0));

        tabs = new JTabbedPane();

        attendanceTab = new AttendanceTab();
        tabs.add(attendanceTab, "Attendance");

        performanceTab = new PerformanceTab();
        tabs.add(performanceTab.performanceTab(), "Performance");

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

}
