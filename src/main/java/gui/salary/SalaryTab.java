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
    private Details details;
    private SalaryUpToDayInSubjectMonth salaryUpToDayInSubjectMonth;
    private Compute compute;
    private SalaryInput salaryInput;
    private Payable payable;
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
        c.gridwidth = 2;
        panelInputs.add(employeeCard, c);

        JPanel panelHorizontalDetailsWithInputs = new JPanel();
        details = new Details();
        c = new GridBagConstraints();
        c.gridy = 1;
        panelHorizontalDetailsWithInputs.add(details.getContainer());

        JPanel panelVerticalInputWithCompute = new JPanel(new GridBagLayout());
        salaryInput = new SalaryInput();
        c = new GridBagConstraints();
        c.gridy = 0;
        panelVerticalInputWithCompute.add(salaryInput.getSalaryInputsPanel(), c);
        salaryUpToDayInSubjectMonth = new SalaryUpToDayInSubjectMonth();
        salaryUpToDayInSubjectMonth.setSpinnerMonthDayslModel(salaryInput.getYearMonthSubjectOfSalary());
        salaryUpToDayInSubjectMonth.setSalaryInput(salaryInput);
        salaryInput.addSubjectDateChangeListener(salaryUpToDayInSubjectMonth);
        c = new GridBagConstraints();
        c.gridy = 1;
        panelVerticalInputWithCompute.add(salaryUpToDayInSubjectMonth.getContainer(), c);
        compute = new Compute();
        compute.setDetails(details);
        compute.setSalaryUpToDayInSubjectMonth(salaryUpToDayInSubjectMonth);
        salaryUpToDayInSubjectMonth.addSalaryUpToDateSpinnerCheckedListener(compute);
        salaryInput.addSubjectDateChangeListener(compute);
        JPanel panelComputeWithPayable = new JPanel();
        payable = new Payable();
        panelComputeWithPayable.add(compute.getBtnCompute());
        panelComputeWithPayable.add(payable.getContainer());
        salaryInput.addSubjectDateChangeListener(payable);
        c = new GridBagConstraints();
        c.gridy = 2;
        panelVerticalInputWithCompute.add(panelComputeWithPayable, c);
        panelHorizontalDetailsWithInputs.add(panelVerticalInputWithCompute);
        c = new GridBagConstraints();
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 2;
        panelInputs.add(panelHorizontalDetailsWithInputs, c);
        payable.setSalaryInput(salaryInput);
        compute.addPaymnetListener(payable);
        compute.addComputeListener(payable);

        salarySubmit = new SalarySubmit();
        salarySubmit.setPayable(payable);
        salaryInput.setCompute(compute);
        compute.addComputeListener(salaryInput);
        compute.addPaymnetListener(payable);
        salaryInput.addSubjectDateChangeListener(salarySubmit);
        compute.addComputeListener(salarySubmit);
        compute.addPaymnetListener(salarySubmit);
        compute.setSalaryInput(salaryInput);
        salaryUpToDayInSubjectMonth.addSalaryUpToDateSpinnerCheckedListener(salarySubmit);
        salarySubmit.addSalaryCreatedListener(salaryInput);
        salarySubmit.addSalaryCreatedListener(payable);
        salarySubmit.addSalaryCreatedListener(compute);
        salarySubmit.addSalaryUpdatedListener(salaryInput);
        salarySubmit.addSalaryUpdatedICRPListener(salaryInput);

        salaryCancel = new SalaryCancel();
        salaryCancel.addCancelListener(salaryInput);
        salaryCancel.addCancelListener(salarySubmit);
        salarySubmit.addSalaryUpdatedListener(salaryCancel);
        salarySubmit.addSalaryUpdatedICRPListener(salaryCancel);
        c = new GridBagConstraints();
        c.gridy = 4;
        c.gridx = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.LINE_START;
        // top, left, buttom, right
        c.insets = new Insets(3, 31, 5, 0);
        panelInputs.add(salaryCancel.getButtonCancel(), c);

        salarySubmit.setSalaryInput(salaryInput);
        c = new GridBagConstraints();
        c.gridy = 4;
        c.gridx = 1;
        c.weightx = 1.0;
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
        salaryRecords.addDeleteListener(salaryInput);
        salaryRecords.addRowSelectedListener(salaryInput);
        salaryCancel.addCancelListener(salaryRequest);
        salaryCancel.addCancelListener(salaryRecords);
        panelReuestRecords.add(salaryRequest.getPanelControls(), BorderLayout.PAGE_START);
        panelReuestRecords.add(salaryRecords.getPanelTable(), BorderLayout.CENTER);

        salaryDisplay = new SalaryDisplay();
        salaryCancel.addCancelListener(salaryDisplay);
        salaryDisplay.addSalaryDisplayableListener(salarySubmit);
        salaryDisplay.addSalaryDisplayableListener(salaryInput);
        salaryDisplay.addSalaryDisplayableListener(salaryCancel);
        salaryDisplay.addSalaryDisplayableListener(salaryRequest);
        salaryDisplay.addSalaryDisplayableListener(salaryRecords);
        salarySubmit.addSalaryUpdatedListener(salaryDisplay);
        salarySubmit.addSalaryUpdatedICRPListener(salaryDisplay);
        salaryRequest.getPanelControls().add(salaryDisplay.getCheckDisplayMode());

        salaryEdit = new SalaryEdit();
        salaryEdit.addSalaryEditableListener(salaryRequest);
        salaryEdit.addSalaryEditableListener(salaryRecords);
        salaryEdit.addSalaryEditableListener(salaryInput);
        salaryEdit.addSalaryEditableListener(salaryCancel);
        salaryEdit.addSalaryEditableListener(salaryDisplay);
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

    public Details getDetails() {
        return details;
    }

    public SalaryUpToDayInSubjectMonth getSalaryUpToDayInSubjectMonth() {
        return salaryUpToDayInSubjectMonth;
    }

    public Compute getCompute() {
        return this.compute;
    }

    public Payable getPayable() {
        return this.payable;
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
        return salaryInput;
    }

}
