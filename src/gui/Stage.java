/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.attendance.AttendanceTab;
import gui.attendancedeductions.AttendanceDeductionTab;
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
        // Attendance panel
        AttendanceTab attendancePanel = controls.getManageEmployee();
        treeEmployees.addEmployeeSelectedListener(attendancePanel.getEmployeeCard());
        treeEmployees.addEmployeeSelectedListener(attendancePanel.getEmployeeDailyAbsence());
        treeEmployees.addEmployeeSelectedListener(attendancePanel.getSubmitAttendancePanel());
        treeEmployees.addEmployeeSelectedListener(attendancePanel.getMonthelyAbsence());
        // Attendance deductions panel
        AttendanceDeductionTab attendanceDeductionPanel = controls.getAttendanceDeductionPanel();
        treeEmployees.addEmployeeSelectedListener(attendanceDeductionPanel.geteEmployeeCard());
        treeEmployees.addEmployeeSelectedListener(attendanceDeductionPanel.getMonthAttendanceDeductions());
        // Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(5);
        JScrollPane scrollPane = new JScrollPane(treeEmployees.getTree());
        splitPane.setTopComponent(scrollPane);
        splitPane.setBottomComponent(controls);
        splitPane.setDividerLocation(100);
        this.add(splitPane);
    }
}
