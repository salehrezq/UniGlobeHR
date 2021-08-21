/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

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
import logic.SetEmployeeAsAbsentAction;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class ManageEmployee extends JPanel {

    private final GridBagLayout gridbag;
    private static JLabel lbEmpName;
    private static JLabel lbDateEnrollment;
    private static JButton btnSetAbsent;
    private static DatePicker datePicker;
    public static final String UNSELECTED = "UN-SELECTED";

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

        btnSetAbsent = new JButton("Set Absent");
        btnSetAbsent.setEnabled(false);
        btnSetAbsent.addActionListener(new SetEmployeeAsAbsentAction());
        panelAbsentSet.add(btnSetAbsent);

        datePicker = new DatePicker();
        datePicker.setTodayAsDefault();
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

    public static LocalDate getSelectedDate() {
        return ManageEmployee.datePicker.getDate();
    }

    public static JButton getAbsentButton() {
        return btnSetAbsent;
    }

}
