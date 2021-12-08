package gui.performance;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Saleh
 */
public class PerformanceSubmit {

    private JPanel mainPanel;
    private JButton btnSubmit;
    private PerformanceInput performanceInput;

    public PerformanceSubmit() {

        mainPanel = new JPanel();
        btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(new SubmitPerformance());
        mainPanel.add(btnSubmit);

    }

    public void setPerformanceInput(PerformanceInput performanceInput) {
        this.performanceInput = performanceInput;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    class SubmitPerformance implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            System.out.println("Time " + performanceInput.getTime());
            System.out.println("Date " + performanceInput.getDate());
            System.out.println("State " + performanceInput.getStateOfPerformance());
            System.out.println("Type " + performanceInput.getType());
            System.out.println("Amount " + performanceInput.getAmount());
            System.out.println("Title " + performanceInput.getTitle());
            System.out.println("Description " + performanceInput.getDescription());
        }
    }

}
