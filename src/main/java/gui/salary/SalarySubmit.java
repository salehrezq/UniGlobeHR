package gui.salary;

import crud.CreateListener;
import crud.UpdateICRPListener;
import crud.UpdateListener;
import datalink.CRUDSalary;
import gui.EmployeeSelectedListener;
import gui.MenuItemSalaryDeleteListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import model.Employee;
import model.Salary;
import gui.MenuItemSalaryUpToDateListener;

/**
 *
 * @author Saleh
 */
public class SalarySubmit
        implements
        Subject,
        EmployeeSelectedListener,
        DisplayableListener,
        CancelListener,
        RowSelectedListener,
        RowDeselectedListener,
        ComputeListener,
        SubjectDateChangeListener,
        PaymnetListener,
        MenuItemSalaryUpToDateListener,
        MenuItemSalaryDeleteListener,
        SalaryUpToDateSpinnerCheckedListener {

    private Operation operation;
    private JButton btnSubmit;
    private SalaryInput salaryInput;
    private Payable payable;
    private StringBuilder stringBuilder;
    private Employee employee;
    private List<CreateListener> createListeners;
    private List<UpdateListener> updateListeners;
    private List<UpdateICRPListener> updateICRPListeners;
    private boolean boolSalaryDisplayMode, boolPaymentCleared, boolSalaryPaid;
    private Integer salaryId;
    private Salary salaryBeforeUpdate;

    public SalarySubmit() {

        createListeners = new ArrayList<>();
        updateListeners = new ArrayList<>();
        updateICRPListeners = new ArrayList<>();

        btnSubmit = new JButton();
        btnSubmit.setEnabled(false);
        btnSubmit.addActionListener(new SubmitSalary());
        stringBuilder = new StringBuilder(145);

        switchBtnToCreateOperation();
    }

    public void setSalaryInput(SalaryInput salaryInput) {
        this.salaryInput = salaryInput;
    }

    public void setPayable(Payable payable) {
        this.payable = payable;
    }

    public void addSalaryCreatedListener(CreateListener createListener) {
        this.createListeners.add(createListener);
    }

    private void notifyCreated() {
        this.createListeners.forEach((createListener) -> {
            createListener.created();
        });
    }

    public void addSalaryUpdatedListener(UpdateListener updateListener) {
        this.updateListeners.add(updateListener);
    }

    private void notifyUpdated() {
        this.updateListeners.forEach((updateListener) -> {
            updateListener.updated();
        });
    }

    public void addSalaryUpdatedICRPListener(UpdateICRPListener updateicrpl) {
        this.updateICRPListeners.add(updateicrpl);
    }

    private void notifyUpdatedICRP() {
        this.updateICRPListeners.forEach((updateicrpl) -> {
            updateicrpl.updatedICRP();
        });
    }

    public JButton getSubmitButton() {
        return btnSubmit;
    }

    private String prepareInputsFailMessage(List<String> failMessages) {

        this.stringBuilder.setLength(0);

        failMessages.stream().forEach(msg -> {
            this.stringBuilder.append(msg).append("\n");
        });

        return this.stringBuilder.toString();
    }

    private LocalDate getYearMonthSubjectOfSalary() {
        int year = salaryInput.getSubjectYear();
        Month month = Month.of(salaryInput.getSubjectMonth());

        LocalDate yearMonthSubjectOfSalary = LocalDate.of(year, month, 1);

        return yearMonthSubjectOfSalary;
    }

    @Override
    public void displayable() {
        boolSalaryDisplayMode = true;
        btnSubmit.setEnabled(false);
    }

    @Override
    public void unDisplayable() {
        boolSalaryDisplayMode = false;
        if (employee != null) {
            operation = new CreateOperation();
            operation.switchOperationFor(this);
            // btnSubmit.setEnabled(true);
        }
    }

    @Override
    public void cancelled() {
        if (boolSalaryDisplayMode) {
            btnSubmit.setEnabled(false);
        }
    }

    @Override
    public JButton getOperationButton() {
        return this.btnSubmit;
    }

    @Override
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public Operation getOperation() {
        return this.operation;
    }

    private void switchBtnToCreateOperation() {
        operation = new CreateOperation();
        operation.switchOperationFor(this);
    }

    private void switchBtnToDeleteOperation() {
        operation = new DeleteOperation();
        operation.switchOperationFor(this);
    }

    @Override
    public void rowSelectedWithRecordId(int id) {
        salaryId = id;
    }

    @Override
    public void rowDeselection() {
        salaryId = null;
    }

    @Override
    public void computed(BigDecimal amount) {
        if (!boolPaymentCleared) {
            btnSubmit.setEnabled(true);
        }
    }

    @Override
    public void yearOrMonthChanged(YearMonth yearMonth) {
        btnSubmit.setEnabled(false);
    }

    @Override
    public void yearAndMonthNotChanged(YearMonth yearMonth) {
        btnSubmit.setEnabled(false);
    }

    @Override
    public void cleared() {
        boolPaymentCleared = true;
        btnSubmit.setEnabled(false);
    }

    @Override
    public void pending() {
        boolPaymentCleared = false;
        btnSubmit.setEnabled(true);
    }

    @Override
    public void deleteAbility(boolean enable) {
        // btnSubmit.setEnabled(false);
        if (enable) {
            btnSubmit.setEnabled(true);
            switchBtnToDeleteOperation();
        } else {
            btnSubmit.setEnabled(false);
            switchBtnToCreateOperation();
        }
    }

    @Override
    public void spinnerChecked(boolean checked) {
        btnSubmit.setEnabled(false);
    }

    @Override
    public void salaryUpToDateAbility(boolean enable) {
        btnSubmit.setEnabled(false);
    }

    private class ValidateWithMessages {

        private List<String> messages;
        private List<Boolean> booleans;

        public ValidateWithMessages(List<Boolean> booleans, List<String> messages) {
            this.booleans = booleans;
            this.messages = messages;
        }
    }

    private ValidateWithMessages validateSalaryInputs() {

        List<String> messages = new ArrayList<>();
        List<Boolean> booleans = new ArrayList<>();

        if (salaryInput.getBoolDateFilled()) {
            booleans.add(true);
        } else {
            booleans.add(false);
            messages.add("Date: empty input");
        }
        return new ValidateWithMessages(booleans, messages);
    }

    @Override
    public void employeeSelected(Employee employee) {
        btnSubmit.setEnabled(false);
        if (boolPaymentCleared && !boolSalaryDisplayMode) {
//            btnSubmit.setEnabled(true);
        }
        this.employee = employee;
        boolSalaryPaid = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), salaryInput.getYearMonthSubjectOfSalary()) != null;
        if (boolSalaryPaid) {
            switchBtnToDeleteOperation();
        } else {
            switchBtnToCreateOperation();
        }
    }

    @Override
    public void employeeDeselected() {
        btnSubmit.setEnabled(false);
        this.employee = null;
    }

    private static void showMessage(String message, boolean state) {
        JOptionPane.showConfirmDialog(null,
                message, state ? "Success" : "Error",
                JOptionPane.DEFAULT_OPTION, state ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    class SubmitSalary implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            ValidateWithMessages validateWithMessages = validateSalaryInputs();

            // Check if List of boolean values are all true or one value at least is false
            // Important note: If booleans.size() is zero;
            // the result of allMatch() is always true.
            // So you have to check the size first, or depending on context needs.
            boolean areAllInputsFilled = validateWithMessages.booleans.stream().allMatch(Boolean::booleanValue);

            if (!areAllInputsFilled) {
                JOptionPane.showConfirmDialog(null,
                        prepareInputsFailMessage(validateWithMessages.messages), "Incorrect inputs or empty fields",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }

            Salary salaryRetrieved = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), getYearMonthSubjectOfSalary());
            boolean boolCreateOperation = getOperation() instanceof CreateOperation;
            boolean boolDeleteOperation = getOperation() instanceof DeleteOperation;
            boolean boolEmployeeWithYearMonthSubjectExist = salaryRetrieved != null;

            String month = Month.of(salaryInput.getSubjectMonth()).toString();
            month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
            int year = salaryInput.getSubjectYear();

            if (boolCreateOperation && boolEmployeeWithYearMonthSubjectExist) {
                JOptionPane.showConfirmDialog(null,
                        "Salary for the month of " + month + " " + year + " was already inserted.",
                        "Info", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (boolDeleteOperation && !boolEmployeeWithYearMonthSubjectExist) {
                JOptionPane.showConfirmDialog(null,
                        "No salary was paid as of " + month + " " + year + " to delete.",
                        "Info", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Salary salary = null;

            if (boolCreateOperation) {
                salary = new Salary();

                salary.setEmployeeId(employee.getId());
                salary.setAgreedSalary(employee.getSalary());
                salary.setYearMonthSubject(getYearMonthSubjectOfSalary());
                salary.setDateGiven(salaryInput.getDateSalaryGiven());
                salary.setPayable(payable.getPayable());
            } else if (boolDeleteOperation) {
                salary = salaryRetrieved;
            }

            // Create|Delete
            boolean submitted = operation.post(salary);

            String informMessage = "No operation!";
            if (boolCreateOperation) {
                if (submitted) {
                    notifyCreated();
                    btnSubmit.setEnabled(false);
                    switchBtnToDeleteOperation();
                    informMessage = "Salary created successfully.";
                } else {
                    informMessage = "Issue regarding Salary create.";
                }
            } else if (boolDeleteOperation) {
                if (submitted) {
                    btnSubmit.setEnabled(false);
                    switchBtnToCreateOperation();
                    informMessage = "Salary deleted successfully.";
                } else {
                    informMessage = "Issue regarding Salary delete.";
                }
            }
            showMessage(informMessage, submitted);
        }
    }
}
