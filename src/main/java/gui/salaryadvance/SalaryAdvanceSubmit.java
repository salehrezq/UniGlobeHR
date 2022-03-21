package gui.salaryadvance;

import crud.CreateListener;
import crud.UpdateListener;
import gui.EmployeeSelectedListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
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
public class SalaryAdvanceSubmit
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
    private SalaryAdvanceInput salaryAdvanceInput;
    private StringBuilder stringBuilder;
    private Employee employee;
    private List<CreateListener> createListeners;
    private List<UpdateListener> updateListeners;
    private boolean boolPerformanceDisplayMode, boolEditMode;
    private Integer performanceId;

    public SalaryAdvanceSubmit() {

        createListeners = new ArrayList<>();
        updateListeners = new ArrayList<>();

        btnSubmit = new JButton();
        btnSubmit.setEnabled(false);
        btnSubmit.addActionListener(new SubmitPerformance());
        stringBuilder = new StringBuilder(145);

        operation = new CreateOperation();
        operation.switchOperationFor(this);
    }

    public void setSalaryAdvanceInput(SalaryAdvanceInput salaryAdvanceInput) {
        this.salaryAdvanceInput = salaryAdvanceInput;
    }

    public void addPerformanceUpdatedListener(UpdateListener updateListener) {
        this.updateListeners.add(updateListener);
    }

    private void notifyUpdated() {
        this.updateListeners.forEach((updateListener) -> {
            updateListener.updated();
        });
    }

    public void addPerformanceCreatedListener(CreateListener createListener) {
        this.createListeners.add(createListener);
    }

    private void notifyCreated() {
        this.createListeners.forEach((createListener) -> {
            createListener.created();
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

    private YearMonth getYearMonthSubjectOfAdvance() {

        Year year = Year.parse(salaryAdvanceInput.getSubjectYear());
        Month month = Month.of(salaryAdvanceInput.getSubjectMonth());

        YearMonth yearMonthSubjectOfAdvance = YearMonth.of(Integer.parseInt(year.toString()), month);

        System.out.println(yearMonthSubjectOfAdvance);
        return yearMonthSubjectOfAdvance;
    }

    @Override
    public void displayable() {
        boolPerformanceDisplayMode = true;
        btnSubmit.setEnabled(false);
    }

    @Override
    public void unDisplayable() {
        boolPerformanceDisplayMode = false;
        if (employee != null) {
            operation = new CreateOperation();
            operation.switchOperationFor(this);
            btnSubmit.setEnabled(true);
        }
    }

    @Override
    public void editable() {
        boolEditMode = true;
        operation = new UpdateOperation();
        operation.switchOperationFor(this);
        btnSubmit.setEnabled(true);
    }

    @Override
    public void cancelled() {
        if (boolPerformanceDisplayMode) {
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
        performanceId = id;
    }

    @Override
    public void rowDeselection() {
        performanceId = null;
    }

    private class ValidateWithMessages {

        private List<String> messages;
        private List<Boolean> booleans;

        public ValidateWithMessages(List<Boolean> booleans, List<String> messages) {
            this.booleans = booleans;
            this.messages = messages;
        }
    }

    private ValidateWithMessages validatePerformanceInputs() {

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
        if (!boolPerformanceDisplayMode) {
            btnSubmit.setEnabled(true);
        }
        this.employee = employee;
    }

    @Override
    public void employeeDeselected() {
        btnSubmit.setEnabled(false);
        this.employee = null;
    }

    class SubmitPerformance implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            ValidateWithMessages validateWithMessages = validatePerformanceInputs();

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
                    salaryAdvance.setId(performanceId);
                }
                // Create|Update
                boolean submitted = operation.post(salaryAdvance);

                if (submitted) {
                    notifyCreated();

                    String informMessage = null;

                    if (getOperation() instanceof CreateOperation) {
                        notifyCreated();
                        informMessage = "Performance created successfully.";
                    } else if (getOperation() instanceof UpdateOperation) {
                        boolEditMode = false;
                        notifyUpdated();
                        btnSubmit.setEnabled(false);
                        informMessage = "Performance updated successfully.";
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
