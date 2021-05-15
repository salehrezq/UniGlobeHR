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

/**
 *
 * @author Saleh
 */
public class NewEmployeeDialog extends JDialog {

    private JFrame parentFrame;
    private JPanel panel;
    private int width;
    private int height;
    private GridBagConstraints gbc;
    private JLabel lbName;
    private JLabel lbDate;
    private JTextField tfName;

    public NewEmployeeDialog(JFrame parentFrame, String title, boolean modal) {
        this.width = 400;
        this.height = 500;
        this.parentFrame = parentFrame;
        panel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();

        fieldName();
        fieldDate();

        this.setSize(new Dimension(width, height));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
                setSize(width, height);
            }
        });

        this.getContentPane().add(panel);
        this.setVisible(true);

    }

    private void fieldName() {
        lbName = new JLabel("Name");
        tfName = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lbName, gbc);
        gbc.gridx++;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(tfName, gbc);
    }

    private void fieldDate() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        // Don't know about the formatter, but there it is...
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.addActionListener((arg0) -> {
            Date selectedDate = (Date) datePicker.getModel().getValue();
            LocalDate date = LocalDate.ofInstant(selectedDate.toInstant(), ZoneId.systemDefault());
            System.out.println(date);
        });
        lbDate = new JLabel("Date of enrollment");
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lbDate, gbc);
        gbc.gridx++;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(datePicker, gbc);
    }

}
