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
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 *
 * @author Saleh
 */
public class Stage extends JPanel {

    private TreeEmployees treeEmployees;
    private Controls controls;

    public Stage() {
        super();
    }

    public void createStage() {

        this.setLayout(new GridLayout(1, 0));
        treeEmployees = new TreeEmployees();
        controls = new Controls();
        // Attendance tab panel
        AttendanceTab attendancePanel = controls.getAttendanceTab();
        treeEmployees.addEmployeeSelectedListener(attendancePanel.getEmployeeCard());
        treeEmployees.addEmployeeSelectedListener(attendancePanel.getEmployeeDailyAbsence());
        treeEmployees.addEmployeeSelectedListener(attendancePanel.getSubmitAttendancePanel());
        treeEmployees.addEmployeeSelectedListener(attendancePanel.getMonthAttendanceDeductions());

        // Performance tab panel
        PerformanceTab performanceTab = controls.getPerformanceTab();
        treeEmployees.addEmployeeSelectedListener(performanceTab.getEmployeeCard());
        treeEmployees.addEmployeeSelectedListener(performanceTab.getPerformanceInput());
        treeEmployees.addEmployeeSelectedListener(performanceTab.getPerformanceSubmit());
        treeEmployees.addEmployeeSelectedListener(performanceTab.getPerformanceRequest());
        treeEmployees.addEmployeeSelectedListener(performanceTab.getPerformanceRecords());

        // Salary Advance tab panel
        SalaryAdvanceTab salaryAdvanceTab = controls.getSalaryAdvanceTab();
        treeEmployees.addEmployeeSelectedListener(salaryAdvanceTab.getEmployeeCard());
        treeEmployees.addEmployeeSelectedListener(salaryAdvanceTab.getSalaryAdvanceInput());
        treeEmployees.addEmployeeSelectedListener(salaryAdvanceTab.getSalaryAdvanceSubmit());
        treeEmployees.addEmployeeSelectedListener(salaryAdvanceTab.getSalaryAdvanceRequest());
        treeEmployees.addEmployeeSelectedListener(salaryAdvanceTab.getSalaryAdvanceRecords());

        // Salary tab panel
        SalaryTab salaryTab = controls.getSalaryTab();
        treeEmployees.addEmployeeSelectedListener(salaryTab.getPayables());

        // Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(5);
        JScrollPane scrollPane = new JScrollPane(treeEmployees.getTree());
        splitPane.setTopComponent(scrollPane);
        splitPane.setBottomComponent(controls);
        splitPane.setDividerLocation(100);
        this.setPreferredSize(new Dimension(1000, 500));
        this.add(splitPane);
    }
}
