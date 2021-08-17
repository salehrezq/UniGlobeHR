/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class ManageEmployee extends JPanel {

    private GridBagLayout gridbag;
    private JButton btnInsertEmployee;
    private static JLabel lbEmpName;
    private static JLabel lbDateEnrollment;

    public ManageEmployee() {

        super();
        this.setBorder(BorderFactory.createLineBorder(Color.red));

        gridbag = new GridBagLayout();

        this.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();

        lbEmpName = new JLabel();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.insets = new Insets(5, 5, 0, 0);
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(lbEmpName, c);

        lbDateEnrollment = new JLabel();
        c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 0, 0);
        c.weighty = 1.0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        this.add(lbDateEnrollment, c);
    }

    public static void setLabelEmpName(String empName) {
        lbEmpName.setText("Name: " + empName);
    }

    public static void setLbDateEnrollment(String aLbDateEnrollment) {
        lbDateEnrollment.setText("Enrollment date: " + aLbDateEnrollment);
    }

}
