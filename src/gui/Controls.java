/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Saleh
 */
public class Controls extends JPanel {

    private JTabbedPane tabs;
    private ManageEmployees manageEmployees;

    public Controls() {
        super();
        this.setLayout(new GridLayout(1, 0));

        manageEmployees = new ManageEmployees();
        tabs = new JTabbedPane();
        tabs.add(manageEmployees, "Employees CRUD");
        this.add(tabs);

    }

}
