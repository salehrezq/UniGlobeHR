package gui.performance;

import datalink.CRUDPerformanceType;
import gui.DateDeselectedListener;
import gui.DateListener;
import gui.DatePicker;
import gui.EmployeeSelectedListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.Employee;
import model.PerformanceType;

/**
 *
 * @author Saleh
 */
public class PerformanceInput implements EmployeeSelectedListener {

    private JPanel mainPanel;
    private JPanel panelStoryInputs;
    private JPanel panelMetaInputs;
    private JLabel lbTime;
    private JTextField fieldTime;
    private DatePicker datePicker;
    private JComboBox comboStateOfPerformance;
    private JComboBox comboType;
    private JLabel lbAmount;
    private JTextField fieldAmount;
    private JTextField tfTitle;
    private JScrollPane scrollableTextArea;
    private JTextArea taDescription;
    private Color colorFieldRight;
    private Color colorFieldWrong;

    public PerformanceInput() {

        panelMetaInputs = new JPanel();

        GridBagConstraints c;

        colorFieldRight = new Color(226, 252, 237);
        colorFieldWrong = new Color(254, 225, 214);

        lbTime = new JLabel("Time:");
        panelMetaInputs.add(lbTime);

        DocumentRegex docRegx = new DocumentRegex();

        fieldTime = new JTextField();
        fieldTime.setFont(new Font("SansSerif", Font.BOLD, 12));
        fieldTime.setBackground(colorFieldRight);
        fieldTime.getDocument().addDocumentListener(docRegx);
        fieldTime.setPreferredSize(new Dimension(60, 27));
        panelMetaInputs.add(fieldTime);

        datePicker = new DatePicker();
        datePicker.setTodayAsDefault();
        DateListenerImpli dateListenerImpli = new DateListenerImpli();
        datePicker.addDateListener(dateListenerImpli);
        datePicker.addDateDeselectedListener(dateListenerImpli);
        panelMetaInputs.add(datePicker.getDatePicker());

        comboStateOfPerformance = new JComboBox<>(new String[]{"Select...", "Positive", "Negative"});
        comboStateOfPerformance.addItemListener(new ItemChangeListener());
        comboStateOfPerformance.setSelectedIndex(0);
        panelMetaInputs.add(comboStateOfPerformance);

        comboType = new JComboBox<>();
        comboType.setPreferredSize(new Dimension(145, 25));
        panelMetaInputs.add(comboType);

        lbAmount = new JLabel("Amount+:");
        panelMetaInputs.add(lbAmount);

        fieldAmount = new JTextField();
        fieldAmount.setFont(new Font("SansSerif", Font.BOLD, 12));
        fieldAmount.setBackground(colorFieldRight);
        fieldAmount.getDocument().addDocumentListener(docRegx);
        fieldAmount.setPreferredSize(new Dimension(95, 27));
        panelMetaInputs.add(fieldAmount);

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
//        System.out.println(employee.getName());
    }

    @Override
    public void employeeDeselected() {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getTime() {
        return fieldTime.getText();
    }

    public LocalDate getDate() {
        return datePicker.getDate();
    }

    public String getStateOfPerformance() {
        return (String) comboStateOfPerformance.getSelectedItem();
    }

    public int getType() {
        return comboType.getSelectedIndex();
    }

    public String getAmount() {
        return fieldAmount.getText();
    }

    public String getTitle() {
        return tfTitle.getText();
    }

    public String getDescription() {
        return taDescription.getText();
    }

    private class DateListenerImpli implements DateListener, DateDeselectedListener {

        @Override
        public void dateChanged(LocalDate date) {
//            System.out.println(date);
        }

        @Override
        public void dateDeselected() {
            System.out.println("PerformanceInput dateDeselected");
        }
    }

    class DocumentRegex implements DocumentListener {

        private void doWork() {

            if (fieldTime.getText().isEmpty()) {
                fieldTime.setBackground(colorFieldRight);
            } else {
                if (fieldTime.getText().matches("^(0[1-9]|1[0-2]):([0-5][0-9]) ((a|p)m|(A|P)M)$")) {
                    fieldTime.setBackground(colorFieldRight);
                } else {
                    fieldTime.setBackground(colorFieldWrong);
                }
            }

            if (fieldAmount.getText().isEmpty()) {
                fieldAmount.setBackground(colorFieldRight);
            } else {
                // Regex match DECIMAL(12,3)
                if (fieldAmount.getText().matches("^\\d{0,9}(?:(?<=\\d)\\.(?=\\d)\\d{0,3})?$")) {
                    fieldAmount.setBackground(colorFieldRight);
                } else {
                    fieldAmount.setBackground(colorFieldWrong);
                }
            }
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            doWork();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            doWork();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            doWork();
        }

    }

    private void populateComboTypes(boolean state) {
        List<PerformanceType> performanceTypes = CRUDPerformanceType.getPerformanceTypesByState(state);
        comboType.removeAllItems();
        performanceTypes.stream().forEach(pType -> {
            comboType.addItem(pType.getType());
        });
    }

    class ItemChangeListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent event) {
            Object source = event.getSource();
            if (source == comboStateOfPerformance) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    if (event.getItem().equals("Negative")) {
                        populateComboTypes(false);
                    } else if (event.getItem().equals("Positive")) {
                        populateComboTypes(true);
                    } else if (comboStateOfPerformance.getSelectedIndex() == 0) {
                        comboType.removeAllItems();
                    }
                }
            }
        }
    }
}
