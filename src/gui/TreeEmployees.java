/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import datalink.CRUDEmployee;
import java.util.TreeSet;

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

    public TreeEmployees() {
        super();
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

    private void createEmployeesNodes(DefaultMutableTreeNode top) {

        DefaultMutableTreeNode employeeNode = null;

        TreeSet<Employee> employees = (TreeSet<Employee>) CRUDEmployee.getAll();

        for (Employee c : employees) {
            employeeNode = new DefaultMutableTreeNode(c.getName());
            top.add(employeeNode);
        }
    }

    public static void addEmployeeNode(Employee employee) {
        DefaultMutableTreeNode employeeNode = new DefaultMutableTreeNode(employee.getName());
        TreeEmployees.laborers.add(employeeNode);

        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.reload(TreeEmployees.laborers); // notify changes to model
        tree.expandPath(tree.getSelectionPath());
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        Object nodeInfo = node.getUserObject();
        if (node.isLeaf()) {
            System.out.println("Leaf");
        } else {
            System.out.println("Not leaf");
        }
    }
}
