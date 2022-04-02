package gui.salary;

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
public class SalaryTab {

    private JPanel panelContainer, panelInputs;
    private EmployeeCard employeeCard;
    private SalaryInput SalaryAdvanceInput;
    private SalarySubmit salaryAdvanceSubmit;
    private SalaryCancel salaryAdvanceCancel;
    private final JPanel panelReuestRecords;
    private SalaryRequest salaryAdvanceRequest;
    private SalaryRecords salaryAdvanceRecords;
    private SalaryDisplay salaryAdvanceDisplay;
    private SalaryEdit salaryAdvanceEdit;

    public SalaryTab() {

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

        salaryAdvanceSubmit = new SalarySubmit();
        SalaryAdvanceInput = new SalaryInput();
        salaryAdvanceSubmit.addSalaryAdvanceCreatedListener(SalaryAdvanceInput);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedListener(SalaryAdvanceInput);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedICRPListener(SalaryAdvanceInput);
        c = new GridBagConstraints();
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 0.5;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_START;
        panelInputs.add(SalaryAdvanceInput.getSalaryAdvanceInputsPanel(), c);

        salaryAdvanceCancel = new SalaryCancel();
        salaryAdvanceCancel.addCancelListener(SalaryAdvanceInput);
        salaryAdvanceCancel.addCancelListener(salaryAdvanceSubmit);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedListener(salaryAdvanceCancel);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedICRPListener(salaryAdvanceCancel);
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

        salaryAdvanceRequest = new SalaryRequest();
        salaryAdvanceRecords = new SalaryRecords();
        salaryAdvanceRequest.addReadListener(salaryAdvanceRecords);

        salaryAdvanceRecords.addRowSelectedListener(salaryAdvanceSubmit);
        salaryAdvanceRecords.addRowDeselectedListenerListener(salaryAdvanceSubmit);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedListener(salaryAdvanceRequest);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedICRPListener(salaryAdvanceRequest);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedListener(salaryAdvanceRecords);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedICRPListener(salaryAdvanceRecords);
        salaryAdvanceRecords.addDeleteListener(SalaryAdvanceInput);
        salaryAdvanceRecords.addRowSelectedListener(SalaryAdvanceInput);
        salaryAdvanceCancel.addCancelListener(salaryAdvanceRequest);
        salaryAdvanceCancel.addCancelListener(salaryAdvanceRecords);
        panelReuestRecords.add(salaryAdvanceRequest.getPanelControls(), BorderLayout.PAGE_START);
        panelReuestRecords.add(salaryAdvanceRecords.getPanelTable(), BorderLayout.CENTER);

        salaryAdvanceDisplay = new SalaryDisplay();
        salaryAdvanceCancel.addCancelListener(salaryAdvanceDisplay);
        salaryAdvanceDisplay.addSalaryAdvanceDisplayableListener(salaryAdvanceSubmit);
        salaryAdvanceDisplay.addSalaryAdvanceDisplayableListener(SalaryAdvanceInput);
        salaryAdvanceDisplay.addSalaryAdvanceDisplayableListener(salaryAdvanceCancel);
        salaryAdvanceDisplay.addSalaryAdvanceDisplayableListener(salaryAdvanceRequest);
        salaryAdvanceDisplay.addSalaryAdvanceDisplayableListener(salaryAdvanceRecords);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedListener(salaryAdvanceDisplay);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedICRPListener(salaryAdvanceDisplay);
        salaryAdvanceRequest.getPanelControls().add(salaryAdvanceDisplay.getCheckDisplayMode());

        salaryAdvanceEdit = new SalaryEdit();
        salaryAdvanceEdit.addSalaryAdvanceEditableListener(salaryAdvanceRequest);
        salaryAdvanceEdit.addSalaryAdvanceEditableListener(salaryAdvanceRecords);
        salaryAdvanceEdit.addSalaryAdvanceEditableListener(SalaryAdvanceInput);
        salaryAdvanceEdit.addSalaryAdvanceEditableListener(salaryAdvanceCancel);
        salaryAdvanceEdit.addSalaryAdvanceEditableListener(salaryAdvanceDisplay);
        salaryAdvanceEdit.addSalaryAdvanceEditableListener(salaryAdvanceSubmit);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedListener(salaryAdvanceEdit);
        salaryAdvanceSubmit.addSalaryAdvanceUpdatedICRPListener(salaryAdvanceEdit);
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

    public JPanel getSalaryTab() {
        return panelContainer;
    }

    public EmployeeCard getEmployeeCard() {
        return employeeCard;
    }

    public SalarySubmit getSalaryAdvanceSubmit() {
        return salaryAdvanceSubmit;
    }

    public SalaryRequest getSalaryAdvanceRequest() {
        return salaryAdvanceRequest;
    }

    public SalaryRecords getSalaryAdvanceRecords() {
        return salaryAdvanceRecords;
    }

    public SalaryInput getSalaryAdvanceInput() {
        return SalaryAdvanceInput;
    }

}
