package gui.salaryadvance;

import gui.EmployeeCard;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.JPanel;

/**
 *
 * @author Saleh
 */
public class SalaryAdvanceTab {

    private JPanel panelContainer, panelInputs;
    private EmployeeCard employeeCard;
    private SalaryAdvanceInput SalaryAdvanceInput;
    private SalaryAdvanceSubmit salaryAdvanceSubmit;
    private SalaryAdvanceCancel salaryAdvanceCancel;
    private final JPanel panelReuestRecords;
    private SalaryAdvanceRequest salaryAdvanceRequest;
    private SalaryAdvanceRecords salaryAdvanceRecords;
    private SalaryAdvanceDisplay salaryAdvanceDisplay;
    private SalaryAdvanceEdit salaryAdvanceEdit;

    public SalaryAdvanceTab() {

        GridBagConstraints c;

        panelInputs = new JPanel(new GridBagLayout());
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

        salaryAdvanceSubmit = new SalaryAdvanceSubmit();
        SalaryAdvanceInput = new SalaryAdvanceInput();
        salaryAdvanceSubmit.addSalaryAdvanceCreatedListener(SalaryAdvanceInput);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedListener(SalaryAdvanceInput);
        c = new GridBagConstraints();
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 0.5;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_START;
        panelInputs.add(SalaryAdvanceInput.getSalaryAdvanceInputsPanel(), c);

        salaryAdvanceCancel = new SalaryAdvanceCancel();
        salaryAdvanceCancel.addCancelListener(SalaryAdvanceInput);
        salaryAdvanceCancel.addCancelListener(salaryAdvanceSubmit);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedListener(salaryAdvanceCancel);
        c = new GridBagConstraints();
        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.LINE_START;
        // top, left, buttom, right
        c.insets = new Insets(3, 31, 5, 0);
        panelInputs.add(salaryAdvanceCancel.getButtonCancel(), c);

        salaryAdvanceSubmit.setSalaryAdvanceInput(SalaryAdvanceInput);
        c = new GridBagConstraints();
        c.gridy = 2;
        c.gridx = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.LINE_END;
        // top, left, buttom, right
        c.insets = new Insets(3, 0, 5, 31);
        panelInputs.add(salaryAdvanceSubmit.getSubmitButton(), c);

        salaryAdvanceRequest = new SalaryAdvanceRequest();
        salaryAdvanceRecords = new SalaryAdvanceRecords();
        salaryAdvanceRequest.addReadListener(salaryAdvanceRecords);

        salaryAdvanceRecords.addRowSelectedListener(salaryAdvanceSubmit);
        salaryAdvanceRecords.addRowDeselectedListenerListener(salaryAdvanceSubmit);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedListener(salaryAdvanceRequest);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedListener(salaryAdvanceRecords);
        salaryAdvanceRecords.addDeleteListener(SalaryAdvanceInput);
        salaryAdvanceRecords.addRowSelectedListener(SalaryAdvanceInput);
        salaryAdvanceCancel.addCancelListener(salaryAdvanceRequest);
        salaryAdvanceCancel.addCancelListener(salaryAdvanceRecords);
        panelReuestRecords.add(salaryAdvanceRequest.getPanelControls(), BorderLayout.PAGE_START);
        panelReuestRecords.add(salaryAdvanceRecords.getPanelTable(), BorderLayout.CENTER);

        salaryAdvanceDisplay = new SalaryAdvanceDisplay();
        salaryAdvanceCancel.addCancelListener(salaryAdvanceDisplay);
        salaryAdvanceDisplay.addSalaryAdvanceDisplayableListener(salaryAdvanceSubmit);
        salaryAdvanceDisplay.addSalaryAdvanceDisplayableListener(SalaryAdvanceInput);
        salaryAdvanceDisplay.addSalaryAdvanceDisplayableListener(salaryAdvanceCancel);
        salaryAdvanceDisplay.addSalaryAdvanceDisplayableListener(salaryAdvanceRequest);
        salaryAdvanceDisplay.addSalaryAdvanceDisplayableListener(salaryAdvanceRecords);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedListener(salaryAdvanceDisplay);
        salaryAdvanceRequest.getPanelControls().add(salaryAdvanceDisplay.getCheckDisplayMode());

        salaryAdvanceEdit = new SalaryAdvanceEdit();
        salaryAdvanceEdit.addSalaryAdvanceEditableListener(salaryAdvanceRequest);
        salaryAdvanceEdit.addSalaryAdvanceEditableListener(salaryAdvanceRecords);
        salaryAdvanceEdit.addSalaryAdvanceEditableListener(SalaryAdvanceInput);
        salaryAdvanceEdit.addSalaryAdvanceEditableListener(salaryAdvanceCancel);
        salaryAdvanceEdit.addSalaryAdvanceEditableListener(salaryAdvanceDisplay);
        salaryAdvanceEdit.addSalaryAdvanceEditableListener(salaryAdvanceSubmit);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedListener(salaryAdvanceEdit);
        salaryAdvanceCancel.addCancelListener(salaryAdvanceEdit);
        salaryAdvanceRecords.addRowDeselectedListenerListener(salaryAdvanceEdit);
        salaryAdvanceRecords.addRowSelectedListener(salaryAdvanceEdit);
        salaryAdvanceDisplay.addSalaryAdvanceDisplayableListener(salaryAdvanceEdit);
        salaryAdvanceRequest.getPanelControls().add(Box.createHorizontalStrut(30));
        salaryAdvanceRequest.getPanelControls().add(salaryAdvanceEdit.getBtnEditMode());

        panelContainer = new JPanel(new BorderLayout());
        panelContainer.add(panelInputs, BorderLayout.PAGE_START);
        panelContainer.add(panelReuestRecords, BorderLayout.CENTER);
    }

    public JPanel salaryAdvanceTab() {
        return panelContainer;
    }

    public EmployeeCard getEmployeeCard() {
        return employeeCard;
    }

    public SalaryAdvanceSubmit getSalaryAdvanceSubmit() {
        return salaryAdvanceSubmit;
    }

    public SalaryAdvanceRequest getSalaryAdvanceRequest() {
        return salaryAdvanceRequest;
    }

    public SalaryAdvanceRecords getSalaryAdvanceRecords() {
        return salaryAdvanceRecords;
    }

    public SalaryAdvanceInput getSalaryAdvanceInput() {
        return SalaryAdvanceInput;
    }

}
