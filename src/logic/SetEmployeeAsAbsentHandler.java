/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import datalink.CRUDAttendance;
import gui.DateListener;
import gui.ManageEmployee;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import model.Attendance;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class SetEmployeeAsAbsentHandler implements ActionListener {

    private static Employee employee;
    private static LocalDate selectedDate;

    @Override
    public void actionPerformed(ActionEvent arg0) {
        Attendance attendance = new Attendance();
        attendance.setEmployeeId(employee.getId());
        attendance.setDate(selectedDate);
        int result = CRUDAttendance.create(attendance);
        if (result == 1) {
            ManageEmployee.abilityBtnSetAbsent(false);
        } else if (result == -1) {
            System.out.println("Already inserted");
        }
    }

    public static void setEmployeeContext(Employee employee) {
        SetEmployeeAsAbsentHandler.employee = employee;
    }

    public void setAbsentDate(LocalDate date) {
        SetEmployeeAsAbsentHandler.selectedDate = date;
    }

}
