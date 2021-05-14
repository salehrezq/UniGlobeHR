/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
        menubar.add(menu);
    }

    public JMenuBar getMenuBar() {
        return this.menubar;
    }

}
