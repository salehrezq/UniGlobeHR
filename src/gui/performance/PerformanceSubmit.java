package gui.performance;

import gui.EmployeeSelectedListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class PerformanceSubmit implements EmployeeSelectedListener {

    private JPanel mainPanel;
    private JButton btnSubmit;
    private PerformanceInput performanceInput;
    private StringBuilder stringBuilder;

    public PerformanceSubmit() {

        mainPanel = new JPanel();
        btnSubmit = new JButton("Submit");
        btnSubmit.setEnabled(false);
        btnSubmit.addActionListener(new SubmitPerformance());
        mainPanel.add(btnSubmit);
        stringBuilder = new StringBuilder(145);

    }

    public void setPerformanceInput(PerformanceInput performanceInput) {
        this.performanceInput = performanceInput;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private String prepareInputsFailMessage(List<String> failMessages) {

        this.stringBuilder.setLength(0);

        failMessages.stream().forEach(msg -> {
            this.stringBuilder.append(msg).append("\n");
        });

        return this.stringBuilder.toString();
    }

    private void checkFilledInputs() {

        List<String> messages = new ArrayList<>();
        List<Boolean> booleans = new ArrayList<>();

        if (performanceInput.getBoolTfTimeFilled()) {
            booleans.add(true);
        } else {
            booleans.add(false);
            messages.add("Time: input either incorrect or empty");
        }
        if (performanceInput.getBoolDateFilled()) {
            booleans.add(true);
        } else {
            booleans.add(false);
            messages.add("Date: empty input");
        }
        if (performanceInput.getBoolComboState()) {
            booleans.add(true);
        } else {
            booleans.add(false);
            messages.add("State: not selected");
        }
        if (performanceInput.getBoolComboType()) {
            booleans.add(true);
        } else {
            booleans.add(false);
            messages.add("Type: not selected");
        }
        if (performanceInput.getBoolTfAmountFilled()) {
            booleans.add(true);
        } else {
            booleans.add(false);
            messages.add("Amount: input either incorrect or empty");
        }
        if (!performanceInput.getTitle().isBlank() && performanceInput.getTitle().length() >= 10) {
            booleans.add(true);
        } else {
            booleans.add(false);
            messages.add("Title: empty input");
        }

        // Check if List of boolean values are all true or one value at least is false
        // Important note: If booleans.size() is zero;
        // the result of allMatch() is always true.
        // So you have to check the size first, or depending on context needs.
        boolean areAllInputsFilled = booleans.stream().allMatch(Boolean::booleanValue);

        if (areAllInputsFilled) {
            System.out.println("Here you insert values to the database");
        } else {
            JOptionPane.showConfirmDialog(null,
                    prepareInputsFailMessage(messages), "Incorrect inputs or empty fields",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void employeeSelected(Employee employee) {
        btnSubmit.setEnabled(true);
    }

    @Override
    public void employeeDeselected() {
        btnSubmit.setEnabled(false);
    }

    class SubmitPerformance implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            checkFilledInputs();
        }
    }

}
