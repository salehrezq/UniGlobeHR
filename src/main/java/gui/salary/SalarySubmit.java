package gui.salary;

import crud.CreateListener;
import crud.UpdateICRPListener;
import crud.UpdateListener;
import datalink.CRUDSalary;
import gui.EmployeeSelectedListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import model.Employee;
import model.Salary;

/**
 *
 * @author Saleh
 */
public class SalarySubmit
        implements
        Subject,
        EmployeeSelectedListener,
        DisplayableListener,
        EditableListener,
        CancelListener,
        RowSelectedListener,
        RowDeselectedListener,
        ComputeListener {

    private Operation operation;
    private JButton btnSubmit;
    private SalaryInput salaryInput;
    private StringBuilder stringBuilder;
    private Employee employee;
    private List<CreateListener> createListeners;
    private List<UpdateListener> updateListeners;
    private List<UpdateICRPListener> updateICRPListeners;
    private boolean boolSalaryDisplayMode, boolEditMode;
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

        operation = new CreateOperation();
        operation.switchOperationFor(this);
    }

    public void setSalaryInput(SalaryInput salaryInput) {
        this.salaryInput = salaryInput;
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
    public void editable() {
        // Save copy of current record before update
        // for comparsion with the new updated record
        salaryBeforeUpdate = CRUDSalary.getById(salaryId);
        boolEditMode = true;
        operation = new UpdateOperation();
        operation.switchOperationFor(this);
        btnSubmit.setEnabled(true);
    }

    @Override
    public void cancelled() {
        if (boolSalaryDisplayMode) {
            boolEditMode = false;
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

    @Override
    public void rowSelectedWithRecordId(int id) {
        salaryId = id;
    }

    @Override
    public void rowDeselection() {
        salaryId = null;
    }

    @Override
    public void computed() {
        btnSubmit.setEnabled(true);
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
        if (!boolSalaryDisplayMode) {
//            btnSubmit.setEnabled(true);
        }
        this.employee = employee;
    }

    @Override
    public void employeeDeselected() {
        btnSubmit.setEnabled(false);
        this.employee = null;
    }

    class SubmitSalary implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Boolean isEmployeeWithYearMonthSubjectAlreadyInserted
                    = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), getYearMonthSubjectOfSalary());

            if (isEmployeeWithYearMonthSubjectAlreadyInserted == null) {
                JOptionPane.showConfirmDialog(null,
                        "Some database issue",
                        "Info", JOptionPane.ERROR);
                return;
            }
            if (isEmployeeWithYearMonthSubjectAlreadyInserted) {

                String month = Month.of(salaryInput.getSubjectMonth()).toString();
                month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
                int year = salaryInput.getSubjectYear();

                JOptionPane.showConfirmDialog(null,
                        "Salary for the month of " + month + " " + year + " was already inserted.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            ValidateWithMessages validateWithMessages = validateSalaryInputs();

            // Check if List of boolean values are all true or one value at least is false
            // Important note: If booleans.size() is zero;
            // the result of allMatch() is always true.
            // So you have to check the size first, or depending on context needs.
            boolean areAllInputsFilled = validateWithMessages.booleans.stream().allMatch(Boolean::booleanValue);

            if (areAllInputsFilled) {

                Salary salary = new Salary();
                salary.setEmployeeId(employee.getId());
                salary.setAgreedSalary(employee.getSalary());
                salary.setYearMonthSubject(getYearMonthSubjectOfSalary());
                salary.setDateGiven(salaryInput.getDateSalaryGiven());
                salary.setPayable(salaryInput.getPayable());

                if (boolEditMode) {
                    salary.setId(salaryId);
                }
                // Create|Update
                boolean submitted = operation.post(salary);

                if (submitted) {
                    notifyCreated();

                    String informMessage = null;

                    if (getOperation() instanceof CreateOperation) {
                        notifyCreated();
                        informMessage = "Salary created successfully.";
                    } else if (getOperation() instanceof UpdateOperation) {
                        boolEditMode = false;

                        btnSubmit.setEnabled(false);
                        informMessage = "Salary updated successfully.";

                        // Compare the salary subject date between old and updated record
                        if (salary.getYearMonthSubject()
                                .isEqual(salaryBeforeUpdate.getYearMonthSubject())) {
                            notifyUpdated();
                        } else {
                            // If subject_year_month has been changed (different value) then the
                            // record should not appear against the same requested subject_year_month
                            // attributes, because they are no longer relevant after the change
                            notifyUpdatedICRP();
                        }
                    }
                    JOptionPane.showConfirmDialog(null,
                            informMessage,
                            "Info", JOptionPane.PLAIN_MESSAGE);
                }
            } else {
                JOptionPane.showConfirmDialog(null,
                        prepareInputsFailMessage(validateWithMessages.messages), "Incorrect inputs or empty fields",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
