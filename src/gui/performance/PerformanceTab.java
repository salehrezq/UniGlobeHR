package gui.performance;

import gui.EmployeeCard;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 * @author Saleh
 */
public class PerformanceTab {

    JSplitPane splitPaneContainer;
    private JPanel panelInputs;
    private JPanel panelRequests;
    private EmployeeCard employeeCard;
    private PerformanceInput performanceInput;
    private PerformanceRequest performanceRequest;

    public PerformanceTab() {

        GridBagConstraints c;

        panelInputs = new JPanel(new GridBagLayout());
        panelInputs.setPreferredSize(new Dimension(400, 150));
        panelRequests = new JPanel(new BorderLayout());

        employeeCard = new EmployeeCard();
        c = new GridBagConstraints();
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        panelInputs.add(employeeCard, c);

        performanceInput = new PerformanceInput();
        c = new GridBagConstraints();
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_START;
        panelInputs.add(performanceInput.getPanelInputs(), c);

        performanceRequest = new PerformanceRequest();
        c = new GridBagConstraints();
        c.gridy = 2;
        c.weightx = 1.0;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_END;
        panelRequests.add(performanceRequest.getPanelTable(), BorderLayout.CENTER);

        splitPaneContainer = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPaneContainer.setDividerSize(5);
        splitPaneContainer.setTopComponent(panelInputs);
        splitPaneContainer.setBottomComponent(panelRequests);
        splitPaneContainer.setDividerLocation(250);
    }

    public JSplitPane performanceTab() {
        return splitPaneContainer;
    }

    public EmployeeCard getEmployeeCard() {
        return employeeCard;
    }

    public PerformanceRequest getPerformanceRequest() {
        return performanceRequest;
    }

    public PerformanceInput getPerformanceInput() {
        return performanceInput;
    }

}
