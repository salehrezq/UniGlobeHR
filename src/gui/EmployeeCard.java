/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class EmployeeCard extends JPanel implements EmployeeSelectedListener {

    private final GridBagLayout gridbag;

    private static JLabel lbEmpName;
    private static JLabel lbDateEnrollment;
    public static final String UNSELECTED = "UN-SELECTED";

    public EmployeeCard() {
        super();

        gridbag = new GridBagLayout();
        this.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();

        lbEmpName = new JLabel("Name: " + UNSELECTED);
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 0, 0);
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(lbEmpName, c);

        lbDateEnrollment = new JLabel("Enrollment Date: " + UNSELECTED);
        c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 0, 0);
        c.gridy = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(lbDateEnrollment, c);
    }

    public void setLabelEmpName(String empName) {
        lbEmpName.setText("Name: " + empName);
    }

    public void setLbDateEnrollment(String aLbDateEnrollment) {
        lbDateEnrollment.setText("Enrollment Date: " + aLbDateEnrollment);
    }

    @Override
    public void employeeSelected(Employee employee) {
        this.setLabelEmpName(employee.getName());
        this.setLbDateEnrollment(employee.getEnrolledDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    @Override
    public void employeeDeselected() {
        this.setLabelEmpName(UNSELECTED);
        this.setLbDateEnrollment(UNSELECTED);
    }
}
