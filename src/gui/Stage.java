/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 * @author Saleh
 */
public class Stage extends JPanel {

    public TreeEmployees treeEmployees;
    public Controls controls;

    public Stage() {
        super();
    }

    public void createStage() {

        treeEmployees = new TreeEmployees();
        controls = new Controls();
        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setTopComponent(treeEmployees);
        splitPane.setBottomComponent(controls);
        splitPane.setDividerLocation(100);
        splitPane.setPreferredSize(new Dimension(500, 300));

        this.add(splitPane);

    }

}
