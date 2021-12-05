package gui.performance;

import gui.DateListener;
import gui.DatePicker;
import gui.EmployeeSelectedListener;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class PerformanceInput implements EmployeeSelectedListener {

    private JPanel mainPanel;
    private JPanel panelStoryInputs;
    private JPanel panelMetaInputs;
    private JTextField tfTitle;
    private JScrollPane scrollableTextArea;
    private JTextArea taDescription;
    private DatePicker datePicker;

    public PerformanceInput() {

        panelMetaInputs = new JPanel();

        GridBagConstraints c;

        datePicker = new DatePicker();
        datePicker.setTodayAsDefault();
        datePicker.addDateListener(new DateListenerImpli());
        panelMetaInputs.add(datePicker.getDatePicker());

        panelStoryInputs = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        tfTitle = new JTextField();
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        panelStoryInputs.add(tfTitle, c);

        c = new GridBagConstraints();
        taDescription = new JTextArea();
        scrollableTextArea = new JScrollPane(taDescription);
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        panelStoryInputs.add(scrollableTextArea, c);

        mainPanel = new JPanel(new GridBagLayout());

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 30, 5, 30);
        c.weightx = 1.0;
        mainPanel.add(panelMetaInputs, c);

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 30, 5, 30);
        c.weightx = 1.0;
        c.weighty = 1.0;
        mainPanel.add(panelStoryInputs, c);
    }

    public JPanel getPerformanceInputsPanel() {
        return mainPanel;
    }

    @Override
    public void employeeSelected(Employee employee) {
        System.out.println(employee.getName());
    }

    @Override
    public void employeeDeselected() {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class DateListenerImpli implements DateListener {

        @Override
        public void dateChanged(LocalDate date) {
            System.out.println(date);
        }
    }

}
