package gui.performance;

import gui.EmployeeCard;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;
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
    private PerformanceSubmit performanceSubmit;
    private PerformanceCancel performanceCancel;
    private PerformanceRequest performanceRequest;
    private PerformanceDisplay performanceDisplay;
    private PerformanceEdit performanceEdit;

    public PerformanceTab() {

        GridBagConstraints c;

        panelInputs = new JPanel(new GridBagLayout());
        panelInputs.setPreferredSize(new Dimension(400, 100));
        panelRequests = new JPanel(new BorderLayout());

        employeeCard = new EmployeeCard();
        c = new GridBagConstraints();
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        panelInputs.add(employeeCard, c);

        performanceSubmit = new PerformanceSubmit();
        performanceInput = new PerformanceInput();
        performanceSubmit.addPerformanceSubmittedListener(performanceInput);
        c = new GridBagConstraints();
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 0.5;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_START;
        panelInputs.add(performanceInput.getPerformanceInputsPanel(), c);

        performanceCancel = new PerformanceCancel();
        performanceCancel.setPerformanceInput(performanceInput);
        c = new GridBagConstraints();
        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.LINE_START;
        // top, left, buttom, right
        c.insets = new Insets(3, 31, 5, 0);
        panelInputs.add(performanceCancel.getButtonCancel(), c);

        performanceSubmit.setPerformanceInput(performanceInput);
        c = new GridBagConstraints();
        c.gridy = 2;
        c.gridx = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.LINE_END;
        // top, left, buttom, right
        c.insets = new Insets(3, 0, 5, 31);
        panelInputs.add(performanceSubmit.getSubmitButton(), c);

        performanceRequest = new PerformanceRequest();
        performanceRequest.addRowSelectedListener(performanceInput);
        panelRequests.add(performanceRequest.getPanelTable(), BorderLayout.CENTER);

        performanceDisplay = new PerformanceDisplay();
        performanceDisplay.addPerformanceDisplayableListener(performanceSubmit);
        performanceDisplay.addPerformanceDisplayableListener(performanceInput);
        performanceDisplay.addPerformanceDisplayableListener(performanceCancel);
        performanceDisplay.addPerformanceDisplayableListener(performanceRequest);
        performanceRequest.getPanelControls().add(performanceDisplay.getCheckDisplayMode());

        performanceEdit = new PerformanceEdit();
        performanceEdit.addPerformanceEditableListener(performanceInput);
        performanceEdit.addPerformanceEditableListener(performanceCancel);
        performanceCancel.addCancelListener(performanceEdit);
        performanceRequest.addRowDeselectedListenerListener(performanceEdit);
        performanceRequest.addRowSelectedListener(performanceEdit);
        performanceDisplay.addPerformanceDisplayableListener(performanceEdit);
        performanceRequest.getPanelControls().add(Box.createHorizontalStrut(30));
        performanceRequest.getPanelControls().add(performanceEdit.getBtnEditMode());

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

    public PerformanceSubmit getPerformanceSubmit() {
        return performanceSubmit;
    }

    public PerformanceRequest getPerformanceRequest() {
        return performanceRequest;
    }

    public PerformanceInput getPerformanceInput() {
        return performanceInput;
    }

}
