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
    private Details payables;
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
        payables = new Details();
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
        compute.setPayables(payables);
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
        compute.setSalaryInput(SalaryInput);
        salarySubmit.addSalaryCreatedListener(SalaryInput);
        salarySubmit.addSalaryUpdatedListener(SalaryInput);
        salarySubmit.addSalaryUpdatedICRPListener(SalaryInput);
        c = new GridBagConstraints();
        c.gridy = 3;
        c.weightx = 1.0;
        c.weighty = 0.5;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_START;
        panelInputs.add(SalaryInput.getSalaryInputsPanel(), c);

        salaryCancel = new SalaryCancel();
        salaryCancel.addCancelListener(SalaryInput);
        salaryCancel.addCancelListener(salarySubmit);
        salarySubmit.addSalaryUpdatedListener(salaryCancel);
        salarySubmit.addSalaryUpdatedICRPListener(salaryCancel);
        c = new GridBagConstraints();
        c.gridy = 4;
        c.gridx = 0;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.LINE_START;
        // top, left, buttom, right
        c.insets = new Insets(3, 31, 5, 0);
        panelInputs.add(salaryCancel.getButtonCancel(), c);

        salarySubmit.setSalaryInput(SalaryInput);
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
        salarySubmit.addSalaryUpdatedListener(salaryRequest);
        salarySubmit.addSalaryUpdatedICRPListener(salaryRequest);
        salarySubmit.addSalaryUpdatedListener(salaryRecords);
        salarySubmit.addSalaryUpdatedICRPListener(salaryRecords);
        salaryRecords.addDeleteListener(SalaryInput);
        salaryRecords.addRowSelectedListener(SalaryInput);
        salaryCancel.addCancelListener(salaryRequest);
        salaryCancel.addCancelListener(salaryRecords);
        panelReuestRecords.add(salaryRequest.getPanelControls(), BorderLayout.PAGE_START);
        panelReuestRecords.add(salaryRecords.getPanelTable(), BorderLayout.CENTER);

        salaryDisplay = new SalaryDisplay();
        salaryCancel.addCancelListener(salaryDisplay);
        salaryDisplay.addSalaryDisplayableListener(salarySubmit);
        salaryDisplay.addSalaryDisplayableListener(SalaryInput);
        salaryDisplay.addSalaryDisplayableListener(salaryCancel);
        salaryDisplay.addSalaryDisplayableListener(salaryRequest);
        salaryDisplay.addSalaryDisplayableListener(salaryRecords);
        salarySubmit.addSalaryUpdatedListener(salaryDisplay);
        salarySubmit.addSalaryUpdatedICRPListener(salaryDisplay);
        salaryRequest.getPanelControls().add(salaryDisplay.getCheckDisplayMode());

        salaryEdit = new SalaryEdit();
        salaryEdit.addSalaryEditableListener(salaryRequest);
        salaryEdit.addSalaryEditableListener(salaryRecords);
        salaryEdit.addSalaryEditableListener(SalaryInput);
        salaryEdit.addSalaryEditableListener(salaryCancel);
        salaryEdit.addSalaryEditableListener(salaryDisplay);
        salaryEdit.addSalaryEditableListener(salarySubmit);
        salarySubmit.addSalaryUpdatedListener(salaryEdit);
        salarySubmit.addSalaryUpdatedICRPListener(salaryEdit);
        salaryCancel.addCancelListener(salaryEdit);
        salaryRecords.addRowDeselectedListenerListener(salaryEdit);
        salaryRecords.addRowSelectedListener(salaryEdit);
        salaryDisplay.addSalaryDisplayableListener(salaryEdit);
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

    public Details getPayables() {
        return payables;
    }

    public Compute getCompute() {
        return this.compute;
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
