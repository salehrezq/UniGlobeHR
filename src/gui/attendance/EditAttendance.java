/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendance;

import datalink.CRUDAttendance;
import gui.EmployeeSelectedListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicArrowButton;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class EditAttendance extends JPanel implements EmployeeAttendanceDataListener, DateChangedAttendanceDataListener {

    private final JButton btnEditModeAttendance;

    public EditAttendance() {
        btnEditModeAttendance = new JButton("Edit Mode");
        btnEditModeAttendance.setEnabled(false);
        this.add(btnEditModeAttendance);
    }

    @Override
    public void employeeAttendanceDataOnSelection(CRUDAttendance.EmployeeAttendanceStatus eas) {
        if (eas.getWasAttendanceTaken()) {
            btnEditModeAttendance.setEnabled(true);
        } else {
            btnEditModeAttendance.setEnabled(false);
        }
    }

    @Override
    public void employeeSelectionCleared() {
        btnEditModeAttendance.setEnabled(false);
    }

    @Override
    public void dateChanged(CRUDAttendance.EmployeeAttendanceStatus eas) {
        if (eas.getWasAttendanceTaken()) {
            btnEditModeAttendance.setEnabled(true);
        } else {
            btnEditModeAttendance.setEnabled(false);
        }
    }
}
