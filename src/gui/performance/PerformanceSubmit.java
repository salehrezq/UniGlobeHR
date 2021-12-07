package gui.performance;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Saleh
 */
public class PerformanceSubmit {

    private JPanel mainPanel;
    private JButton btnSubmit;

    public PerformanceSubmit() {

        mainPanel = new JPanel();
        btnSubmit = new JButton("Submit");
        mainPanel.add(btnSubmit);

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
