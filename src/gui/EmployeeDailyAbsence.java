/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JPanel;
import logic.SetEmployeeAsAbsentHandler;

/**
 *
 * @author Saleh
 */
public class EmployeeDailyAbsence extends JPanel {

    private static JButton btnSetAbsent;
    private static DatePicker datePicker;
    private static SetEmployeeAsAbsentHandler setAbsentHandler;
    private static LocalDate dateAbsentSelected;

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
        setAbsentHandler.setAbsentDate(dateAbsentSelected);
//        datePicker.addDateListener(this);
        this.add(datePicker.getDatePicker());
    }

    public static void abilityBtnSetAbsent(boolean bool) {
        btnSetAbsent.setEnabled(bool);
    }

    public static LocalDate getAbsentSelectedDate() {
        return datePicker.getDate();
    }

    public DatePicker getDatePicker() {
        return this.datePicker;
    }

    public void setDateAbsentSelected(LocalDate date) {
        dateAbsentSelected = date;
        setAbsentHandler.setAbsentDate(date);
    }

}
