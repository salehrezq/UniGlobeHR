/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import datalink.CRUDEmployee;
import gui.NewEmployeeDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class InsertEmployeeAction implements ActionListener {

    private NewEmployeeDialog dialog;
    private Employee employee;

    public InsertEmployeeAction(NewEmployeeDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        employee = new Employee();
        employee.setName(dialog.getEmployeeName());
        employee.setEnrolledDate(dialog.getEnrollmentDate());
        employee.setActive(dialog.getEmployeeIsActive());

        CRUDEmployee.create(employee);
    }

}
