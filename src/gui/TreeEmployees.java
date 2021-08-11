/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 *
 * @author Saleh
 */
public class TreeEmployees implements TreeSelectionListener {

    private JTree tree;

    public TreeEmployees() {
        super();
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("All");
        DefaultMutableTreeNode labours = new DefaultMutableTreeNode("Labours");
        top.add(labours);
        createNodes(labours);
        tree = new JTree(top);
        tree.putClientProperty("JTree.lineStyle", "Horizontal");
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);
    }

    public JTree getTree() {
        return this.tree;
    }

    private void createNodes(DefaultMutableTreeNode top) {

        DefaultMutableTreeNode item = null;

        int count = 50;
        for (int i = 0; i < count; i++) {
            item = new DefaultMutableTreeNode("labour " + i);
            top.add(item);
        }

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
