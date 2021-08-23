/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.EmployeeController;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import logic.SetEmployeeAsAbsentHandler;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class ManageEmployee extends JPanel implements DateListener, EmployeeSelectedListener {

    private final GridBagLayout gridbag;
    private static JLabel lbEmpName;
    private static JLabel lbDateEnrollment;
    private static JButton btnSetAbsent;
    private static DatePicker datePicker;
    private static SetEmployeeAsAbsentHandler setAbsentHandler;
    private static LocalDate dateAbsentSelected;
    public static final String UNSELECTED = "UN-SELECTED";
    private Employee currentSelectedEmployee;
    private EmployeeController employeeController;

    public ManageEmployee() {

        super();
        this.setBorder(BorderFactory.createLineBorder(Color.red));

        gridbag = new GridBagLayout();

        this.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();

        lbEmpName = new JLabel("Name: " + UNSELECTED);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.insets = new Insets(5, 5, 0, 0);
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(lbEmpName, c);

        lbDateEnrollment = new JLabel("Enrollment Date: " + UNSELECTED);
        c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 0, 0);
        c.gridy = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(lbDateEnrollment, c);

        JPanel panelAbsentSet = new JPanel(new FlowLayout());

        datePicker = new DatePicker();
        setAbsentHandler = new SetEmployeeAsAbsentHandler();
        btnSetAbsent = new JButton("Set Absent");
        btnSetAbsent.setEnabled(false);
        btnSetAbsent.addActionListener(setAbsentHandler);
        panelAbsentSet.add(btnSetAbsent);

        // initial date value settings
        datePicker.setTodayAsDefault();
        dateAbsentSelected = datePicker.getDate();
        setAbsentHandler.setAbsentDate(dateAbsentSelected);
        datePicker.addDateListener(this);
        panelAbsentSet.add(datePicker.getDatePicker());

        c = new GridBagConstraints();
        c.gridy = 2;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(panelAbsentSet, c);

    }

    public static void setLabelEmpName(String empName) {
        lbEmpName.setText("Name: " + empName);
    }

    public static void setLbDateEnrollment(String aLbDateEnrollment) {
        lbDateEnrollment.setText("Enrollment Date: " + aLbDateEnrollment);
    }

    public static void abilityBtnSetAbsent(boolean bool) {
        btnSetAbsent.setEnabled(bool);
    }

    public static LocalDate getAbsentSelectedDate() {
        return datePicker.getDate();
    }

    public void setEmployeeController(EmployeeController employeeController) {
        this.employeeController = employeeController;
    }

    @Override
    public void dateChanged(LocalDate date) {
        ManageEmployee.dateAbsentSelected = date;
        setAbsentHandler.setAbsentDate(date);
        if (this.currentSelectedEmployee != null) {
            this.employeeController.checkIfEmplyeeIsAreadyAbsent(currentSelectedEmployee.getId(), date);
        }
    }

    @Override
    public void employeeSelected(Employee employee) {
        this.currentSelectedEmployee = employee;
    }

    @Override
    public void employeeDeselected() {
        this.currentSelectedEmployee = null;
    }
}
