/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import mainpackage.MainRun;

/**
 *
 * @author Saleh
 */
public class Menu {

    private JMenuBar menubar;
    private JMenu menu;
    private JMenuItem menuInsertEmployee;

    public Menu() {

        super();

    }

    public void createMenuBar() {
        menubar = new JMenuBar();
        menu = new JMenu("File");
        menuInsertEmployee = new JMenuItem("New Employee");
        menu.add(menuInsertEmployee);
        menuInsertEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                new NewEmployeeDialog(MainRun.getFrame(), "Create New Employee", true);
            }
        });
        menubar.add(menu);
    }

    public JMenuBar getMenuBar() {
        return this.menubar;
    }

}
