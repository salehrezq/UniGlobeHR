/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class ManageEmployees extends JPanel {

    private GridBagLayout gridbag;
    private JButton btnInsertEmployee;
    private static JLabel lbEmpName;

    public ManageEmployees() {

        super();
        gridbag = new GridBagLayout();

        this.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        lbEmpName = new JLabel();
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        gridbag.setConstraints(lbEmpName, c);
        this.add(lbEmpName);

        btnInsertEmployee = new JButton("Button 1");
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        gridbag.setConstraints(btnInsertEmployee, c);
        this.add(btnInsertEmployee);
    }

    public static void setLabelEmpName(String empName) {
        lbEmpName.setText(empName);
    }

}
