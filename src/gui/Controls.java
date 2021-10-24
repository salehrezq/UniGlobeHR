/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.attendance.AttendanceTab;
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

    public Controls() {
        super();
        this.setLayout(new GridLayout(1, 0));

        attendancePanel = new AttendanceTab();
        tabs = new JTabbedPane();
        tabs.add(attendancePanel, "Attendance");
        this.add(tabs);
    }

    public AttendanceTab getManageEmployee() {
        return this.attendancePanel;
    }

}
