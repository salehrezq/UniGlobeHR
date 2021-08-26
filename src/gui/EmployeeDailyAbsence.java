/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import datalink.CRUDAttendance;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JPanel;
import model.Attendance;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class EmployeeDailyAbsence extends JPanel implements DateListener, EmployeeSelectedListener {

    private static JButton btnSetAbsent;
    private static DatePicker datePicker;
    private static SetEmployeeAsAbsentHandler setAbsentHandler;
    private static LocalDate dateAbsentSelected;
    private Employee employeeContext;

    public EmployeeDailyAbsence() {
        super();

        datePicker = new DatePicker();
        setAbsentHandler = new SetEmployeeAsAbsentHandler();
        btnSetAbsent = new JButton("Set Absent");
        btnSetAbsent.setEnabled(false);
        btnSetAbsent.addActionListener(setAbsentHandler);
        this.add(btnSetAbsent);

        // initial date value settings
        datePicker.setTodayAsDefault();
        dateAbsentSelected = datePicker.getDate();
        datePicker.addDateListener(this);
//        datePicker.addDateListener(this);
        this.add(datePicker.getDatePicker());
    }

    public void abilityBtnSetAbsent(boolean bool) {
        btnSetAbsent.setEnabled(bool);
    }

    public LocalDate getAbsentSelectedDate() {
        return datePicker.getDate();
    }

    public void setDateAbsentSelected(LocalDate date) {
        dateAbsentSelected = date;
    }

    public void setEmployeeContext(Employee e) {
        this.employeeContext = e;
    }

    @Override
    public void dateChanged(LocalDate date) {
        this.setDateAbsentSelected(date);
        if (this.employeeContext != null) {
            if (CRUDAttendance.isEmployeeAbsentAtSpecificDate(employeeContext.getId(), date)) {
                abilityBtnSetAbsent(false);
            } else {
                abilityBtnSetAbsent(true);
            }
        }
    }

    @Override
    public void employeeSelected(Employee employee) {
        if (CRUDAttendance.isEmployeeAbsentAtSpecificDate(employeeContext.getId(), datePicker.getDate())) {
            abilityBtnSetAbsent(false);
        } else {
            abilityBtnSetAbsent(true);
        }
    }

    @Override
    public void employeeDeselected() {
        abilityBtnSetAbsent(false);
    }

    private class SetEmployeeAsAbsentHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            Attendance attendance = new Attendance();
            attendance.setEmployeeId(employeeContext.getId());
            attendance.setDate(datePicker.getDate());
            int result = CRUDAttendance.create(attendance);
            if (result == 1) {
                abilityBtnSetAbsent(false);
            } else if (result == -1) {
                System.out.println("Already inserted");
            }
        }

    }

}
