package gui.performance;

import gui.EmployeeCard;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

/**
 *
 * @author Saleh
 */
public class PerformanceTab {

    private JPanel panel;
    private EmployeeCard employeeCard;

    public PerformanceTab() {

        panel = new JPanel(new GridBagLayout());

        employeeCard = new EmployeeCard();
        panel.add(employeeCard);
    }

    public JPanel performanceTab() {
        return panel;
    }

    public EmployeeCard getEmployeeCard() {
        return employeeCard;
    }

}
