package gui.salaryadvance;

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
public class SalaryAdvanceTab {

    JSplitPane splitPaneContainer;
    private JPanel panelInputs;
//    private JPanel panelRequests;
    private EmployeeCard employeeCard;
    private PerformanceInput performanceInput;
    private PerformanceSubmit performanceSubmit;
    private PerformanceCancel performanceCancel;
    private final JPanel panelReuestRecords;
    private PerformanceRequest performanceRequest;
    private PerformanceRecords performanceRecords;
    private PerformanceDisplay performanceDisplay;
    private PerformanceEdit performanceEdit;

    public SalaryAdvanceTab() {

        GridBagConstraints c;

        panelInputs = new JPanel(new GridBagLayout());
        panelInputs.setPreferredSize(new Dimension(400, 100));
        panelReuestRecords = new JPanel(new BorderLayout());

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
        performanceSubmit.addPerformanceCreatedListener(performanceInput);
        performanceSubmit.addPerformanceUpdatedListener(performanceInput);
        c = new GridBagConstraints();
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 0.5;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_START;
        panelInputs.add(performanceInput.getPerformanceInputsPanel(), c);

        performanceCancel = new PerformanceCancel();
        performanceCancel.addCancelListener(performanceInput);
        performanceCancel.addCancelListener(performanceSubmit);
        performanceSubmit.addPerformanceUpdatedListener(performanceCancel);
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
        performanceRecords = new PerformanceRecords();
        performanceRequest.addReadListener(performanceRecords);

        performanceRecords.addRowSelectedListener(performanceSubmit);
        performanceRecords.addRowDeselectedListenerListener(performanceSubmit);
        performanceSubmit.addPerformanceUpdatedListener(performanceRequest);
        performanceSubmit.addPerformanceUpdatedListener(performanceRecords);
        performanceRecords.addDeleteListener(performanceInput);
        performanceRecords.addRowSelectedListener(performanceInput);
        performanceCancel.addCancelListener(performanceRequest);
        performanceCancel.addCancelListener(performanceRecords);
        panelReuestRecords.add(performanceRequest.getPanelControls(), BorderLayout.PAGE_START);
        panelReuestRecords.add(performanceRecords.getPanelTable(), BorderLayout.CENTER);

        performanceDisplay = new PerformanceDisplay();
        performanceCancel.addCancelListener(performanceDisplay);
        performanceDisplay.addPerformanceDisplayableListener(performanceSubmit);
        performanceDisplay.addPerformanceDisplayableListener(performanceInput);
        performanceDisplay.addPerformanceDisplayableListener(performanceCancel);
        performanceDisplay.addPerformanceDisplayableListener(performanceRequest);
        performanceDisplay.addPerformanceDisplayableListener(performanceRecords);
        performanceSubmit.addPerformanceUpdatedListener(performanceDisplay);
        performanceRequest.getPanelControls().add(performanceDisplay.getCheckDisplayMode());

        performanceEdit = new PerformanceEdit();
        performanceEdit.addPerformanceEditableListener(performanceRequest);
        performanceEdit.addPerformanceEditableListener(performanceRecords);
        performanceEdit.addPerformanceEditableListener(performanceInput);
        performanceEdit.addPerformanceEditableListener(performanceCancel);
        performanceEdit.addPerformanceEditableListener(performanceDisplay);
        performanceEdit.addPerformanceEditableListener(performanceSubmit);
        performanceSubmit.addPerformanceUpdatedListener(performanceEdit);
        performanceCancel.addCancelListener(performanceEdit);
        performanceRecords.addRowDeselectedListenerListener(performanceEdit);
        performanceRecords.addRowSelectedListener(performanceEdit);
        performanceDisplay.addPerformanceDisplayableListener(performanceEdit);
        performanceRequest.getPanelControls().add(Box.createHorizontalStrut(30));
        performanceRequest.getPanelControls().add(performanceEdit.getBtnEditMode());

        splitPaneContainer = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPaneContainer.setDividerSize(5);
        splitPaneContainer.setTopComponent(panelInputs);
        splitPaneContainer.setBottomComponent(panelReuestRecords);
        splitPaneContainer.setDividerLocation(250);
    }

    public JSplitPane salaryAdvanceTab() {
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

    public PerformanceRecords getPerformanceRecords() {
        return performanceRecords;
    }

    public PerformanceInput getPerformanceInput() {
        return performanceInput;
    }

}
