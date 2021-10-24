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
import javax.swing.JTabbedPane;

/**
 *
 * @author Saleh
 */
public class Controls extends JPanel {

    private JTabbedPane tabs;
    private AttendanceTab attendancePanel;
    private AttendanceDeductionTab attendanceDeductionPanel;

    public Controls() {
        super();
        this.setLayout(new GridLayout(1, 0));

        tabs = new JTabbedPane();

        attendancePanel = new AttendanceTab();
        tabs.add(attendancePanel, "Attendance");
        attendanceDeductionPanel = new AttendanceDeductionTab();
        tabs.add(attendanceDeductionPanel, "Attendance Calculate");
        this.add(tabs);
    }

    public AttendanceTab getManageEmployee() {
        return this.attendancePanel;
    }

}
