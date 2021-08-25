/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.EmployeeController;
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
public class ManageEmployee extends JPanel implements EmployeeSelectedListener {

    private final GridBagLayout gridbag;

    private Employee currentSelectedEmployee;
    private EmployeeController employeeController;
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

    public void setEmployeeController(EmployeeController employeeController) {
        this.employeeDailyAbsence.setEmployeeController(employeeController);
    }

    @Override
    public void employeeSelected(Employee employee) {
        this.currentSelectedEmployee = employee;
        this.monthelyAbsence.setSelectedEmployeeId(employee.getId());
        this.employeeDailyAbsence.setEmployeeContext(employee);
    }

    @Override
    public void employeeDeselected() {
        this.currentSelectedEmployee = null;
        this.monthelyAbsence.setSelectedEmployeeId(0);
        this.employeeDailyAbsence.setEmployeeContext(null);
    }
}
