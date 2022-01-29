package gui.salary;

import gui.DatePicker;
import gui.EmployeeCard;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

/**
 *
 * @author Saleh
 */
public class SalaryTab {

    private final JPanel container, panelDatePicker, necessaryRedundantHolder;
    private final EmployeeCard employeeCard;
    private final DatePicker datePicker;
    private final SalaryDetail salaryDetail;

    public SalaryTab() {

        container = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        employeeCard = new EmployeeCard();
        c = new GridBagConstraints();
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        container.add(employeeCard, c);

        panelDatePicker = new JPanel();
        datePicker = new DatePicker();
        datePicker.setTodayAsDefault();
        panelDatePicker.add(datePicker.getDatePicker());
        c = new GridBagConstraints();
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        container.add(panelDatePicker, c);

        salaryDetail = new SalaryDetail();
        necessaryRedundantHolder = new JPanel();
        c = new GridBagConstraints();
        c.gridy = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        necessaryRedundantHolder.add(salaryDetail.getContainer());
        container.add(necessaryRedundantHolder, c);
    }

    public JPanel getContainer() {
        return container;
    }

    public EmployeeCard getEmployeeCard() {
        return employeeCard;
    }

    public SalaryDetail getSalaryDetail() {
        return salaryDetail;
    }

}
