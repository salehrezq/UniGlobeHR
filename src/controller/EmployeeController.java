/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import datalink.CRUDAttendance;
import gui.EmployeeSelectedListener;
import gui.ManageEmployee;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import logic.SetEmployeeAsAbsentHandler;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class EmployeeController implements EmployeeSelectedListener {

    private LocalDate selectedDate;
    private Employee employee;

    /**
     * You need to update the GUI view every time this method is called from the
     * controller
     *
     * @param employee
     */
    @Override
    public void employeeSelected(Employee employee) {

        if (employee != null) {
            // Employee node was selected
            ManageEmployee.setLabelEmpName(employee.getName());
            String enrollmentDate = employee.getEnrolledDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            ManageEmployee.setLbDateEnrollment(enrollmentDate);
            SetEmployeeAsAbsentHandler.setEmployeeContext(employee);
            LocalDate date = ManageEmployee.getAbsentSelectedDate();
            checkIfEmplyeeIsAreadyAbsent(employee.getId(), date);
        }
    }

    @Override
    public void employeeDeselected() {
        ManageEmployee.abilityBtnSetAbsent(false);
        ManageEmployee.setLabelEmpName("UN-SELECTED");
        ManageEmployee.setLbDateEnrollment("UN-SELECTED");
    }

    public void checkIfEmplyeeIsAreadyAbsent(int employeeId, LocalDate date) {
        if (CRUDAttendance.isEmployeeAbsentAtSpecificDate(employeeId, date)) {
            ManageEmployee.abilityBtnSetAbsent(false);
        } else {
            ManageEmployee.abilityBtnSetAbsent(true);
        }
    }
}
