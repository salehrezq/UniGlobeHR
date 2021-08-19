/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class SetEmployeeAsAbsentAction implements ActionListener {

    private static Employee employee;

    @Override
    public void actionPerformed(ActionEvent arg0) {
        System.out.println(employee.getName());
    }

    public static void setEmployeeContext(Employee employee) {
        SetEmployeeAsAbsentAction.employee = employee;
    }

}
