/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import datalink.CRUDEmployee;
import java.util.ArrayList;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class TreeEmployees implements TreeSelectionListener {

    private static JTree tree;
    private static DefaultMutableTreeNode laborers;
    private Employee employee;
    private final ArrayList<EmployeeSelectedListener> employeeSelectedListeners;

    public TreeEmployees() {
        super();
        employeeSelectedListeners = new ArrayList<>();
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("All");
        laborers = new DefaultMutableTreeNode("Laborers");
        top.add(laborers);
        createEmployeesNodes(laborers);
        tree = new JTree(top);
        tree.putClientProperty("JTree.lineStyle", "Horizontal");
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);
    }

    public JTree getTree() {
        return this.tree;
    }

    private static void createEmployeesNodes(DefaultMutableTreeNode laborers) {

        DefaultMutableTreeNode employeeNode = null;

        ArrayList<Employee> employees = CRUDEmployee.getAll();

        for (Employee e : employees) {
            employeeNode = new DefaultMutableTreeNode(e);
            laborers.add(employeeNode);
        }
    }

    /**
     * Used when inserting a new Employee node to the tree.
     */
    public static void refreshEmployeesTree() {
        laborers.removeAllChildren();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        createEmployeesNodes(laborers);
        model.reload(TreeEmployees.laborers);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        Object selectedNode = node.getUserObject();

        if (selectedNode instanceof Employee) {
            // Employee node is selected
            employee = (Employee) selectedNode;
            this.notifyEmployeeSelected(employee);
        } else {
            // Non Employee node is selected
            this.notifyEmployeeDeselected();
        }

        if (node.isLeaf()) {
            // System.out.println("Leaf");
        } else {
            //  System.out.println("Not leaf");
        }
    }

    public void addEmployeeSelectedListener(EmployeeSelectedListener esl) {
        this.employeeSelectedListeners.add(esl);
    }

    private void notifyEmployeeSelected(Employee employee) {
        this.employeeSelectedListeners.forEach((esl) -> {
            esl.employeeSelected(employee);
        });
    }

    private void notifyEmployeeDeselected() {
        this.employeeSelectedListeners.forEach((esl) -> {
            esl.employeeDeselected();
        });
    }
}
