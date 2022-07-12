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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
    private List<TabsChangeListener> tabsChangeListeners;

    public Controls() {
        super();
        this.setLayout(new GridLayout(1, 0));
        tabsChangeListeners = new ArrayList<>();

        tabs = new JTabbedPane();

        attendanceTab = new AttendanceTab();
        tabs.add(attendanceTab, "Attendance");

        performanceTab = new PerformanceTab();
        tabs.add(performanceTab.performanceTab(), "Performance");

        salaryAdvanceTab = new SalaryAdvanceTab();
        tabs.add(salaryAdvanceTab.salaryAdvanceTab(), "Salary Advance");

        salaryTab = new SalaryTab();
        tabs.add(salaryTab.getSalaryTab(), "Salary");

        tabs.addChangeListener(new TabsChangeHandler());

        this.add(tabs);
    }

    public AttendanceTab getAttendanceTab() {
        return this.attendanceTab;
    }

    public PerformanceTab getPerformanceTab() {
        return performanceTab;
    }

    public SalaryAdvanceTab getSalaryAdvanceTab() {
        return salaryAdvanceTab;
    }

    public SalaryTab getSalaryTab() {
        return salaryTab;
    }

    public void addTabsChangeListener(TabsChangeListener tcl) {
        this.tabsChangeListeners.add(tcl);
    }

    private void notifyTabSelected(int tab) {
        this.tabsChangeListeners.forEach((tcl) -> {
            tcl.tabSelected(tab);
        });
    }

    private class TabsChangeHandler implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() instanceof JTabbedPane) {
                JTabbedPane pane = (JTabbedPane) e.getSource();
                notifyTabSelected(pane.getSelectedIndex());
            }
        }
    }
}
