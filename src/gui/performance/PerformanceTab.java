package gui.performance;

import gui.EmployeeCard;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

/**
 *
 * @author Saleh
 */
public class PerformanceTab {

    private JPanel panel;
    private EmployeeCard employeeCard;
    private PerformanceRequest performanceRequest;

    public PerformanceTab() {

        panel = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        employeeCard = new EmployeeCard();
        c = new GridBagConstraints();
        c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        panel.add(employeeCard, c);

        performanceRequest = new PerformanceRequest();
        c = new GridBagConstraints();
        c.gridy = 1;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_START;
        panel.add(performanceRequest.getPanelTable(), c);
    }

    public JPanel performanceTab() {
        return panel;
    }

    public EmployeeCard getEmployeeCard() {
        return employeeCard;
    }

    public PerformanceRequest getPerformanceRequest() {
        return performanceRequest;
    }

}
