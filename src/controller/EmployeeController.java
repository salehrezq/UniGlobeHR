/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import datalink.CRUDAttendance;
import gui.ManageEmployee;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import logic.SetEmployeeAsAbsentAction;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class EmployeeController {

    private LocalDate selectedDate;
    private Employee employee;

    /**
     * You need to update the GUI view every time this method is called from the
     * controller
     *
     * @param employee
     */
    public static void employeeNodeChangeSetUp(Employee employee) {
        if (employee != null) {
            // Employee node was selected
            ManageEmployee.setLabelEmpName(employee.getName());
            String enrollmentDate = employee.getEnrolledDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            ManageEmployee.setLbDateEnrollment(enrollmentDate);
            SetEmployeeAsAbsentAction.setEmployeeContext(employee);
            SetEmployeeAsAbsentAction.receiveSelectedDate(ManageEmployee.getSelectedDate());
            checkIfEmplyeeIsAreadyAbsent(employee.getId(), ManageEmployee.getSelectedDate());
        } else {
            // Employee node was not selected
            ManageEmployee.abilityBtnSetAbsent(false);
            ManageEmployee.setLabelEmpName("UN-SELECTED");
            ManageEmployee.setLbDateEnrollment("UN-SELECTED");
        }
    }

    private static void checkIfEmplyeeIsAreadyAbsent(int employeeId, LocalDate date) {
        if (CRUDAttendance.isEmployeeAbsentAtSpecificDate(employeeId, date)) {
            ManageEmployee.abilityBtnSetAbsent(false);
        } else {
            ManageEmployee.abilityBtnSetAbsent(true);
        }
    }
}
