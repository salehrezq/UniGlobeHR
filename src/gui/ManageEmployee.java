/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class ManageEmployee extends JPanel {

    private final GridBagLayout gridbag;

    private EmployeeCard employeeCard;
    private EmployeeDailyAbsence employeeDailyAbsence;
    private MonthelyAbsence monthelyAbsence;

    public ManageEmployee() {

        super();
        this.setBorder(BorderFactory.createLineBorder(Color.red));

        gridbag = new GridBagLayout();

        this.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();

        employeeCard = new EmployeeCard();

        c = new GridBagConstraints();
        c.gridy = 1;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(employeeCard, c);

        employeeDailyAbsence = new EmployeeDailyAbsence();

        c = new GridBagConstraints();
        c.gridy = 2;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(employeeDailyAbsence, c);

        monthelyAbsence = new MonthelyAbsence();

        c = new GridBagConstraints();
        c.gridy = 3;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(monthelyAbsence.getPanelTable(), c);
    }

    public EmployeeCard getEmployeeCard() {
        return this.employeeCard;
    }

    public EmployeeDailyAbsence getEmployeeDailyAbsence() {
        return this.employeeDailyAbsence;
    }

    public MonthelyAbsence getMonthelyAbsence() {
        return this.monthelyAbsence;
    }
}
