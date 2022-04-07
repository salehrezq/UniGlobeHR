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

    private JPanel panelContainer, panelInputs, panelComputeBtn;
    private EmployeeCard employeeCard;
    private Payables payables;
    private Compute compute;
    private SalaryInput SalaryInput;
    private SalarySubmit salarySubmit;
    private SalaryCancel salaryCancel;
    private final JPanel panelReuestRecords;
    private SalaryRequest salaryRequest;
    private SalaryRecords salaryRecords;
    private SalaryDisplay salaryDisplay;
    private SalaryEdit salaryEdit;

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

        JPanel panelHolder = new JPanel();
        payables = new Payables();
        panelHolder.add(payables.getContainer());
        c = new GridBagConstraints();
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        panelInputs.add(panelHolder, c);

        panelComputeBtn = new JPanel();
        compute = new Compute();
        panelComputeBtn.add(Box.createHorizontalStrut(125));
        panelComputeBtn.add(compute.getBtnCompute());
        c = new GridBagConstraints();
        c.gridy = 2;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        panelInputs.add(panelComputeBtn, c);

        salarySubmit = new SalarySubmit();
        SalaryInput = new SalaryInput();
        salarySubmit.addSalaryAdvanceCreatedListener(SalaryInput);
        salarySubmit.addSalaryAdvanceUpdatedListener(SalaryInput);
        salarySubmit.addSalaryAdvanceUpdatedICRPListener(SalaryInput);
        c = new GridBagConstraints();
        c.gridy = 3;
        c.weightx = 1.0;
        c.weighty = 0.5;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_START;
        panelInputs.add(SalaryInput.getSalaryAdvanceInputsPanel(), c);

        salaryCancel = new SalaryCancel();
        salaryCancel.addCancelListener(SalaryInput);
        salaryCancel.addCancelListener(salarySubmit);
        salarySubmit.addSalaryAdvanceUpdatedListener(salaryCancel);
        salarySubmit.addSalaryAdvanceUpdatedICRPListener(salaryCancel);
        c = new GridBagConstraints();
        c.gridy = 4;
        c.gridx = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.LINE_START;
        // top, left, buttom, right
        c.insets = new Insets(3, 31, 5, 0);
        panelInputs.add(salaryCancel.getButtonCancel(), c);

        salarySubmit.setSalaryAdvanceInput(SalaryInput);
        c = new GridBagConstraints();
        c.gridy = 4;
        c.gridx = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.LINE_END;
        // top, left, buttom, right
        c.insets = new Insets(3, 0, 5, 31);
        panelInputs.add(salarySubmit.getSubmitButton(), c);

        salaryRequest = new SalaryRequest();
        salaryRecords = new SalaryRecords();
        salaryRequest.addReadListener(salaryRecords);

        salaryRecords.addRowSelectedListener(salarySubmit);
        salaryRecords.addRowDeselectedListenerListener(salarySubmit);
        salarySubmit.addSalaryAdvanceUpdatedListener(salaryRequest);
        salarySubmit.addSalaryAdvanceUpdatedICRPListener(salaryRequest);
        salarySubmit.addSalaryAdvanceUpdatedListener(salaryRecords);
        salarySubmit.addSalaryAdvanceUpdatedICRPListener(salaryRecords);
        salaryRecords.addDeleteListener(SalaryInput);
        salaryRecords.addRowSelectedListener(SalaryInput);
        salaryCancel.addCancelListener(salaryRequest);
        salaryCancel.addCancelListener(salaryRecords);
        panelReuestRecords.add(salaryRequest.getPanelControls(), BorderLayout.PAGE_START);
        panelReuestRecords.add(salaryRecords.getPanelTable(), BorderLayout.CENTER);

        salaryDisplay = new SalaryDisplay();
        salaryCancel.addCancelListener(salaryDisplay);
        salaryDisplay.addSalaryAdvanceDisplayableListener(salarySubmit);
        salaryDisplay.addSalaryAdvanceDisplayableListener(SalaryInput);
        salaryDisplay.addSalaryAdvanceDisplayableListener(salaryCancel);
        salaryDisplay.addSalaryAdvanceDisplayableListener(salaryRequest);
        salaryDisplay.addSalaryAdvanceDisplayableListener(salaryRecords);
        salarySubmit.addSalaryAdvanceUpdatedListener(salaryDisplay);
        salarySubmit.addSalaryAdvanceUpdatedICRPListener(salaryDisplay);
        salaryRequest.getPanelControls().add(salaryDisplay.getCheckDisplayMode());

        salaryEdit = new SalaryEdit();
        salaryEdit.addSalaryAdvanceEditableListener(salaryRequest);
        salaryEdit.addSalaryAdvanceEditableListener(salaryRecords);
        salaryEdit.addSalaryAdvanceEditableListener(SalaryInput);
        salaryEdit.addSalaryAdvanceEditableListener(salaryCancel);
        salaryEdit.addSalaryAdvanceEditableListener(salaryDisplay);
        salaryEdit.addSalaryAdvanceEditableListener(salarySubmit);
        salarySubmit.addSalaryAdvanceUpdatedListener(salaryEdit);
        salarySubmit.addSalaryAdvanceUpdatedICRPListener(salaryEdit);
        salaryCancel.addCancelListener(salaryEdit);
        salaryRecords.addRowDeselectedListenerListener(salaryEdit);
        salaryRecords.addRowSelectedListener(salaryEdit);
        salaryDisplay.addSalaryAdvanceDisplayableListener(salaryEdit);
        salaryRequest.getPanelControls().add(Box.createHorizontalStrut(30));
        salaryRequest.getPanelControls().add(salaryEdit.getBtnEditMode());

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

    public Payables getPayables() {
        return payables;
    }

    public SalarySubmit getSalarySubmit() {
        return salarySubmit;
    }

    public SalaryRequest getSalaryRequest() {
        return salaryRequest;
    }

    public SalaryRecords getSalaryRecords() {
        return salaryRecords;
    }

    public SalaryInput getSalaryInput() {
        return SalaryInput;
    }

}
