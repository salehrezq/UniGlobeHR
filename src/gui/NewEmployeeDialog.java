/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.util.Date;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.time.LocalDate;
import java.time.ZoneId;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import logic.InsertEmployeeAction;

/**
 *
 * @author Saleh
 */
public class NewEmployeeDialog extends JDialog {

    private JPanel panel;
    private int width;
    private int height;
    private GridBagConstraints gbc;
    private JLabel lbName;
    private JTextField fName;
    private JLabel lbDate;
    private JDatePickerImpl fDatePicker;
    private LocalDate enrollmentDate;
    private JCheckBox fActive;
    private JButton btnInsertEmployee;

    public NewEmployeeDialog(JFrame parentFrame, String title, boolean modal) {
        super(parentFrame, title, modal);

        this.width = 400;
        this.height = 500;

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
        insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(lbName, gbc);
        grid(1, 0);
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(fName, gbc);
    }

    private void fieldDate() {
        lbDate = new JLabel("Date of enrollment:");
        insets(0, 0, 10, 0);
        grid(0, 1);
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(lbDate, gbc);
        grid(1, 1);
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(datePicker(), gbc);
    }

    private JDatePickerImpl datePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        // Don't know about the formatter, but there it is...
        fDatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        fDatePicker.addActionListener((arg0) -> {
            Date selectedDate = (Date) fDatePicker.getModel().getValue();
            if (selectedDate != null) {
                enrollmentDate = LocalDate.ofInstant(selectedDate.toInstant(), ZoneId.systemDefault());
                System.out.println(enrollmentDate);
            }
        });
        return fDatePicker;
    }

    private void fieldActive() {
        fActive = new JCheckBox("Is employee active?");
        fActive.setSelected(true);
        fActive.setHorizontalTextPosition(SwingConstants.LEFT);
        insets(0, 0, 10, 0);
        grid(0, 2);
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(fActive, gbc);
    }

    private void btnInsertEmployee() {
        btnInsertEmployee = new JButton("Insert");
        btnInsertEmployee.addActionListener(new InsertEmployeeAction(this));
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
        return this.enrollmentDate;
    }

    public boolean getEmployeeIsActive() {
        return this.fActive.isSelected();
    }

}
