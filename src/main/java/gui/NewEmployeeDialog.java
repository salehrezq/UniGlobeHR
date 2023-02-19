/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import datalink.CRUDEmployee;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import model.Employee;
import utilities.imagefilechooser.IMGFileChooser;

/**
 *
 * @author Saleh
 */
public class NewEmployeeDialog extends JDialog
        implements
        DateListener,
        DateDeselectedListener {

    private JPanel panel;
    private int width;
    private int height;
    private GridBagConstraints gbc;
    private JLabel lbEmpPhoto, lbName;
    private JTextField fName;
    private JLabel lbDate;
    private LocalDate enrollmetDate;
    private DatePicker datePicker;
    private JLabel lbSalary;
    private JFormattedTextField tfSalary;
    private JCheckBox fActive;
    private JButton btnSetEmpPhoto, btnInsertEmployee;
    private IMGFileChooser IMGFileChooser;

    public NewEmployeeDialog(JFrame parentFrame, String title, boolean modal) {
        super(parentFrame, title, modal);

        this.width = 400;
        this.height = 350;

        panel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();

        fieldName();
        fieldDate();
        fieldSalary();
        fieldActive();
        fieldBtnSetEmpPhoto();
        btnInsertEmployee();

        IMGFileChooser = new IMGFileChooser();
        IMGFileChooser.setParentComponent(this);
        btnSetEmpPhoto.addActionListener(IMGFileChooser);

        this.setSize(new Dimension(width, height));
        this.getContentPane().add(panel);
        this.setVisible(true);

    }

    private void fieldName() {
        lbName = new JLabel("Name:");
        fName = new JTextField(20);
        grid(0, 0);
        insets(0, 10, 10, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(lbName, gbc);
        grid(1, 0);
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(fName, gbc);
    }

    private void fieldDate() {
        lbDate = new JLabel("Date of enrollment:");
        insets(0, 10, 10, 0);
        grid(0, 1);
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(lbDate, gbc);
        grid(1, 1);
        gbc.anchor = GridBagConstraints.LINE_END;

        datePicker = new DatePicker();
        datePicker.setTodayAsDefault();
        datePicker.addDateListener(this);
        datePicker.addDateDeselectedListener(this);
        // initial setting
        enrollmetDate = datePicker.getDate();
        panel.add(datePicker.getDatePicker(), gbc);
    }

    private void fieldSalary() {
        lbSalary = new JLabel("Salary:");
        insets(0, 10, 10, 0);
        grid(0, 2);
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(lbSalary, gbc);
        grid(1, 2);
        gbc.anchor = GridBagConstraints.WEST;

        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);//Remove comma from number greater than 4 digit

        NumberFormatter salaryFormatter = new NumberFormatter(format);
        salaryFormatter.setValueClass(Double.class);
        salaryFormatter.setMinimum(0);
        salaryFormatter.setMaximum(1000000);
        salaryFormatter.setAllowsInvalid(false);
        salaryFormatter.setCommitsOnValidEdit(true);// committ value on each keystroke instead of focus lost

        tfSalary = new JFormattedTextField(format);
        tfSalary.getDocument().addDocumentListener(new DocumentRegex());
        tfSalary.setPreferredSize(new Dimension(100, 20));
        panel.add(tfSalary, gbc);
    }

    private void fieldActive() {
        fActive = new JCheckBox("Is employee active?");
        fActive.setSelected(true);
        fActive.setHorizontalTextPosition(SwingConstants.LEFT);
        insets(0, 6, 10, 0);
        grid(0, 3);
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(fActive, gbc);
    }

    private void fieldBtnSetEmpPhoto() {
        lbEmpPhoto = new JLabel("Set Employee Photo:");
        grid(0, 4);
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(lbEmpPhoto, gbc);
        btnSetEmpPhoto = new JButton("Browse...");
        btnSetEmpPhoto.setHorizontalTextPosition(SwingConstants.LEFT);
        grid(1, 4);
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(btnSetEmpPhoto, gbc);
    }

    private void btnInsertEmployee() {
        btnInsertEmployee = new JButton("Insert");
        btnInsertEmployee.addActionListener(new InsertEmployeeHandler());
        grid(1, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnInsertEmployee, gbc);
    }

    private void grid(int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
    }

    private void insets(int top, int left, int bottom, int right) {
        gbc.insets = new Insets(top, left, bottom, right);
    }

    public String getEmployeeName() {
        return this.fName.getText();
    }

    public LocalDate getEnrollmentDate() {
        return this.enrollmetDate;
    }

    public boolean getEmployeeIsActive() {
        return this.fActive.isSelected();
    }

    @Override
    public void dateChanged(LocalDate date) {
        this.enrollmetDate = date;
        btnInsertEmployee.setEnabled(true);
    }

    @Override
    public void dateDeselected() {
        btnInsertEmployee.setEnabled(false);
        this.enrollmetDate = null;
    }

    private class InsertEmployeeHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {

            String name = getEmployeeName();

            if (name.isBlank()) {
                JOptionPane.showConfirmDialog(panel,
                        "Provide a name", "",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tfSalary.getValue() == null || tfSalary.getValue().toString().isBlank()) {
                JOptionPane.showConfirmDialog(panel,
                        "Provide a salary", "",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }

            Employee employee = new Employee();
            employee.setName(name);
            employee.setEnrolledDate(getEnrollmentDate());

            String salaryString = tfSalary.getValue().toString();
            BigDecimal salary = new BigDecimal(salaryString);

            employee.setSalary(salary);
            employee.setActive(getEmployeeIsActive());

            if (CRUDEmployee.create(employee)) {
                setVisible(false);
                TreeEmployees.refreshEmployeesTree();
            } else {
                JOptionPane.showConfirmDialog(panel,
                        "Creating employee failed", "",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    class DocumentRegex implements DocumentListener {

        private void doWork() {
            // Regex match DECIMAL(12,3)
            if (tfSalary.getText().matches("^\\d{0,9}(?:(?<=\\d)\\.(?=\\d)\\d{0,3})?$")) {
                btnInsertEmployee.setEnabled(true);
            } else {
                btnInsertEmployee.setEnabled(false);
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

}
