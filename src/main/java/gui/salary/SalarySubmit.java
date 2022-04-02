package gui.salary;

import crud.CreateListener;
import crud.UpdateICRPListener;
import crud.UpdateListener;
import datalink.CRUDSalaryAdvance;
import gui.EmployeeSelectedListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import model.Employee;
import model.SalaryAdvance;

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
        RowDeselectedListener {

    private Operation operation;
    private JButton btnSubmit;
    private SalaryInput salaryAdvanceInput;
    private StringBuilder stringBuilder;
    private Employee employee;
    private List<CreateListener> createListeners;
    private List<UpdateListener> updateListeners;
    private List<UpdateICRPListener> updateICRPListeners;
    private boolean boolSalaryAdvanceDisplayMode, boolEditMode;
    private Integer salaryAdvanceId;
    private SalaryAdvance salaryAdvanceBeforeUpdate;

    public SalarySubmit() {

        createListeners = new ArrayList<>();
        updateListeners = new ArrayList<>();
        updateICRPListeners = new ArrayList<>();

        btnSubmit = new JButton();
        btnSubmit.setEnabled(false);
        btnSubmit.addActionListener(new SubmitSalaryAdvance());
        stringBuilder = new StringBuilder(145);

        operation = new CreateOperation();
        operation.switchOperationFor(this);
    }

    public void setSalaryAdvanceInput(SalaryInput salaryAdvanceInput) {
        this.salaryAdvanceInput = salaryAdvanceInput;
    }

    public void addSalaryAdvanceCreatedListener(CreateListener createListener) {
        this.createListeners.add(createListener);
    }

    private void notifyCreated() {
        this.createListeners.forEach((createListener) -> {
            createListener.created();
        });
    }

    public void addSalaryAdvanceUpdatedListener(UpdateListener updateListener) {
        this.updateListeners.add(updateListener);
    }

    private void notifyUpdated() {
        this.updateListeners.forEach((updateListener) -> {
            updateListener.updated();
        });
    }

    public void addSalaryAdvanceUpdatedICRPListener(UpdateICRPListener updateicrpl) {
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

    private LocalDate getYearMonthSubjectOfAdvance() {
        Year year = Year.parse(salaryAdvanceInput.getSubjectYear());
        Month month = Month.of(salaryAdvanceInput.getSubjectMonth());

        LocalDate yearMonthSubjectOfAdvance = LocalDate.of(Integer.parseInt(year.toString()), month, 1);

        return yearMonthSubjectOfAdvance;
    }

    @Override
    public void displayable() {
        boolSalaryAdvanceDisplayMode = true;
        btnSubmit.setEnabled(false);
    }

    @Override
    public void unDisplayable() {
        boolSalaryAdvanceDisplayMode = false;
        if (employee != null) {
            operation = new CreateOperation();
            operation.switchOperationFor(this);
            btnSubmit.setEnabled(true);
        }
    }

    @Override
    public void editable() {
        // Save copy of current record before update
        // for comparsion with the new updated record
        salaryAdvanceBeforeUpdate = CRUDSalaryAdvance.getById(salaryAdvanceId);
        boolEditMode = true;
        operation = new UpdateOperation();
        operation.switchOperationFor(this);
        btnSubmit.setEnabled(true);
    }

    @Override
    public void cancelled() {
        if (boolSalaryAdvanceDisplayMode) {
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
        salaryAdvanceId = id;
    }

    @Override
    public void rowDeselection() {
        salaryAdvanceId = null;
    }

    private class ValidateWithMessages {

        private List<String> messages;
        private List<Boolean> booleans;

        public ValidateWithMessages(List<Boolean> booleans, List<String> messages) {
            this.booleans = booleans;
            this.messages = messages;
        }
    }

    private ValidateWithMessages validateSalaryAdvanceInputs() {

        List<String> messages = new ArrayList<>();
        List<Boolean> booleans = new ArrayList<>();

        if (salaryAdvanceInput.getBoolDateFilled()) {
            booleans.add(true);
        } else {
            booleans.add(false);
            messages.add("Date: empty input");
        }
        if (salaryAdvanceInput.getBoolTfAmountFilled()) {
            booleans.add(true);
        } else {
            booleans.add(false);
            messages.add("Amount: input either incorrect or empty");
        }

        return new ValidateWithMessages(booleans, messages);
    }

    @Override
    public void employeeSelected(Employee employee) {
        if (!boolSalaryAdvanceDisplayMode) {
            btnSubmit.setEnabled(true);
        }
        this.employee = employee;
    }

    @Override
    public void employeeDeselected() {
        btnSubmit.setEnabled(false);
        this.employee = null;
    }

    class SubmitSalaryAdvance implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            ValidateWithMessages validateWithMessages = validateSalaryAdvanceInputs();

            // Check if List of boolean values are all true or one value at least is false
            // Important note: If booleans.size() is zero;
            // the result of allMatch() is always true.
            // So you have to check the size first, or depending on context needs.
            boolean areAllInputsFilled = validateWithMessages.booleans.stream().allMatch(Boolean::booleanValue);

            if (areAllInputsFilled) {

                SalaryAdvance salaryAdvance = new SalaryAdvance();
                salaryAdvance.setEmployeeId(employee.getId());
                salaryAdvance.setYearMonthSubject(getYearMonthSubjectOfAdvance());
                salaryAdvance.setDateTaken(salaryAdvanceInput.getDateAdvanceTaken());
                salaryAdvance.setAmount(salaryAdvanceInput.getAmount());

                if (boolEditMode) {
                    salaryAdvance.setId(salaryAdvanceId);
                }
                // Create|Update
                boolean submitted = operation.post(salaryAdvance);

                if (submitted) {
                    notifyCreated();

                    String informMessage = null;

                    if (getOperation() instanceof CreateOperation) {
                        notifyCreated();
                        informMessage = "Salary advance created successfully.";
                    } else if (getOperation() instanceof UpdateOperation) {
                        boolEditMode = false;

                        btnSubmit.setEnabled(false);
                        informMessage = "Salary advance updated successfully.";

                        // Compare the advance subject date between old and updated record
                        if (salaryAdvance.getYearMonthSubject()
                                .isEqual(salaryAdvanceBeforeUpdate.getYearMonthSubject())) {
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
