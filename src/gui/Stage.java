/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.EmployeeController;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;

/**
 *
 * @author Saleh
 */
public class Stage extends JPanel {

    private TreeEmployees treeEmployees;
    private Controls controls;
    private EmployeeController employeeController;

    public Stage() {
        super();
    }

    public void createStage() {

        this.setLayout(new GridLayout(1, 0));
        treeEmployees = new TreeEmployees();
        employeeController = new EmployeeController();
        treeEmployees.addEmployeeSelectedListener(employeeController);
        controls = new Controls();
        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(5);
        JScrollPane scrollPane = new JScrollPane(treeEmployees.getTree());
        splitPane.setTopComponent(scrollPane);
        splitPane.setBottomComponent(controls);
        splitPane.setDividerLocation(100);
        splitPane.setPreferredSize(new Dimension(500, 300));

        this.add(splitPane);

    }

}
