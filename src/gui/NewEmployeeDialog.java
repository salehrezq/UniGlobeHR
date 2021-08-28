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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class NewEmployeeDialog extends JDialog implements DateListener {

    private JPanel panel;
    private int width;
    private int height;
    private GridBagConstraints gbc;
    private JLabel lbName;
    private JTextField fName;
    private JLabel lbDate;
    private LocalDate enrollmetDate;
    private DatePicker datePicker;
    private JCheckBox fActive;
    private JButton btnInsertEmployee;

    public NewEmployeeDialog(JFrame parentFrame, String title, boolean modal) {
        super(parentFrame, title, modal);

        this.width = 400;
        this.height = 200;

        panel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();

        fieldName();
        fieldDate();
        fieldActive();
        btnInsertEmployee();

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
        // initial setting
        enrollmetDate = datePicker.getDate();
        panel.add(datePicker.getDatePicker(), gbc);
    }

    private void fieldActive() {
        fActive = new JCheckBox("Is employee active?");
        fActive.setSelected(true);
        fActive.setHorizontalTextPosition(SwingConstants.LEFT);
        insets(0, 6, 10, 0);
        grid(0, 2);
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(fActive, gbc);
    }

    private void btnInsertEmployee() {
        btnInsertEmployee = new JButton("Insert");
        btnInsertEmployee.addActionListener(new InsertEmployeeHandler());
        grid(1, 3);
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
    }

    private class InsertEmployeeHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            Employee employee = new Employee();
            employee.setName(getEmployeeName());
            employee.setEnrolledDate(getEnrollmentDate());
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

}
