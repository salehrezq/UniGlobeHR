/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

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

    /**
     * You need to update the GUI view every time this method is called from the
     * controller
     *
     * @param employee
     */
    public static void employeeNodeChangeSetUp(Employee employee) {
        ManageEmployee.setLabelEmpName(employee.getName());
        String enrollmentDate = employee.getEnrolledDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        ManageEmployee.setLbDateEnrollment(enrollmentDate);
        SetEmployeeAsAbsentAction.setEmployeeContext(employee);
        SetEmployeeAsAbsentAction.receiveSelectedDate(ManageEmployee.getSelectedDate());
    }

    public static void isEmployeeSelected(boolean selected) {
        ManageEmployee.abilityBtnSetAbsent(selected);
        if (!selected) {
            ManageEmployee.setLabelEmpName("UN-SELECTED");
            ManageEmployee.setLbDateEnrollment("UN-SELECTED");
        }
    }

}
