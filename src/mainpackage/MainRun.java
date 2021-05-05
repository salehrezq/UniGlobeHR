/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainpackage;

import gui.Stage;
import javax.swing.JFrame;

/**
 *
 * @author Saleh
 */
public class MainRun {

    private static Stage stage;

    public MainRun() {

    }

    private static void createAndShowGUI() {

        stage = new Stage();
        stage.createStage();
        //Create and set up the window.
        JFrame frame = new JFrame("TreeDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.getContentPane().add(stage);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainRun.createAndShowGUI();
            }
        });
    }

}
