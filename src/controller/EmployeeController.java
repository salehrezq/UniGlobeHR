/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import gui.ManageEmployees;
import gui.TreeEmployees;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class EmployeeController {

    private static Employee employee;

    /**
     * You need to update the GUI view every time this method is called from the
     * controller
     *
     * @param employee
     */
    public static void updateViewEmployeeWithModelChange(Employee employee) {
        EmployeeController.employee = employee;
        ManageEmployees.setLabelEmpName(employee.getName());
    }

}
