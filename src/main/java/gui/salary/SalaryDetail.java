package gui.salary;

import gui.EmployeeSelectedListener;
import gui.TwoColumnsLabelsAndFields;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class SalaryDetail implements EmployeeSelectedListener {

    private final JPanel container, panelSalaryComponents;
    private final JLabel lbMonthelySalary, lbAdvances, lbAttendanceDeductions, lbPerformanceGain, lbEffectiveSalary;
    private final String labelInitial = "0.0                   ";

    public SalaryDetail() {

        container = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        JComponent[] components = {
            lbMonthelySalary = new JLabel(labelInitial),
            lbAdvances = new JLabel(labelInitial),
            lbAttendanceDeductions = new JLabel(labelInitial),
            lbPerformanceGain = new JLabel(labelInitial),
            lbEffectiveSalary = new JLabel(labelInitial),};

        String[] labels = {
            "Salary",
            "Salary Advances",
            "Attendance deductions",
            "Performance gain",
            "Effective Salary"
        };

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
        lbMonthelySalary.setText(employee.getSalary().toPlainString());
    }

    @Override
    public void employeeDeselected() {

    }

}
