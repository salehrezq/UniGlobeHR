/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendance;

import gui.EmployeeSelectedListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class SubmitAttendance extends JPanel implements EmployeeSelectedListener, EmployeeAttendanceListener {

    private final JButton btnSubmitAttendance;
    private Boolean isPresent;

    public SubmitAttendance() {
        btnSubmitAttendance = new JButton("Submit");
        btnSubmitAttendance.setEnabled(false);
        this.add(btnSubmitAttendance);
    }

    @Override
    public void employeeSelected(Employee employee) {
        if (isPresent != null) {
            btnSubmitAttendance.setEnabled(true);
        }
    }

    @Override
    public void employeeDeselected() {
        btnSubmitAttendance.setEnabled(false);
    }

    @Override
    public void employeeIsPresent() {
        isPresent = Boolean.TRUE;
        btnSubmitAttendance.setEnabled(true);
    }

    @Override
    public void employeeIsAbsent() {
        isPresent = Boolean.FALSE;
        btnSubmitAttendance.setEnabled(true);
    }

}
