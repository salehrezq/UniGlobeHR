package gui.salary;

/**
 *
 * @author Saleh
 */
import gui.EmployeeSelectedListener;
import gui.TwoColumnsLabelsAndFields;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class Details implements EmployeeSelectedListener {

    private final JPanel container, panelSalaryComponents;
    private JTextField tfMonthelySalary, tfAdvances, tfAttendanceDeductions, tfPerformanceGain;
    private final String labelInitial = "0.0";

    public Details() {

        container = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        JComponent[] components = {
            tfMonthelySalary = new JTextField(10),
            tfAdvances = new JTextField(10),
            tfAttendanceDeductions = new JTextField(10),
            tfPerformanceGain = new JTextField(10)};

        String[] labels = {
            "Agreed Salary",
            "Salary Advances deducted",
            "Attendance deductions",
            "Performance Gain",};

        for (JComponent component : components) {
            ((JTextField) component).setEditable(false);
        }

        panelSalaryComponents = (JPanel) TwoColumnsLabelsAndFields.getTwoColumnLayout(labels, components);
        c = new GridBagConstraints();
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        // top, lef, bottom, right
        c.insets = new Insets(20, 0, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        container.add(panelSalaryComponents, c);
    }

    public JPanel getContainer() {
        return container;
    }

    @Override
    public void employeeSelected(Employee employee) {
        tfMonthelySalary.setText(employee.getSalary().toPlainString());
    }

    @Override
    public void employeeDeselected() {
        throw new UnsupportedOperationException("Payables employeeDeselected");
    }

    public void setTfAttendanceDeductions(String deductionsValue) {
        this.tfAttendanceDeductions.setText(deductionsValue);
    }

}
