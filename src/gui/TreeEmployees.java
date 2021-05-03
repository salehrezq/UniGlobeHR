/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

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
public class TreeEmployees extends JPanel implements TreeSelectionListener {

    private JTree tree;
    private JScrollPane treeView;

    public TreeEmployees() {
        super();
        DefaultMutableTreeNode top
                = new DefaultMutableTreeNode("The Java Series");
        tree = new JTree(top);
        tree.putClientProperty("JTree.lineStyle", "Horizontal");
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);
        treeView = new JScrollPane();
        treeView.add(tree);
    }

    @Override
    public void valueChanged(TreeSelectionEvent arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
