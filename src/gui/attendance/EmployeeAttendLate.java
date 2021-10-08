/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendance;

import datalink.CRUDAttendance;
import gui.EmployeeSelectedListener;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class EmployeeAttendLate extends JPanel
        implements EmployeeAttendanceListener, EmployeeAttendanceDataListener, DateChangedAttendanceDataListener {

    private JCheckBox checkEmployeeLate;
    private JFormattedTextField tfMinutesLate;
    private JLabel lbMinutes;

    public EmployeeAttendLate() {

        checkEmployeeLate = new JCheckBox();
        checkEmployeeLate.setEnabled(false);
        checkEmployeeLate.addItemListener(new CheckBoxHandler());
        tfMinutesLate = new JFormattedTextField(getMaskFormatter());
        tfMinutesLate.setPreferredSize(new Dimension(30, 20));
        tfMinutesLate.setEnabled(false);
        lbMinutes = new JLabel("Minutes");
        lbMinutes.setEnabled(false);

        this.add(checkEmployeeLate);
        this.add(tfMinutesLate);
        this.add(lbMinutes);
    }

    /**
     * This method returns MaskFormatter that enforces 3 digits The # character
     * represent digit, and three of them (###) represents the allowed number of
     * digits.
     *
     * 000 is placeholder.
     *
     * @return MaskFormatter
     */
    private MaskFormatter getMaskFormatter() {
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter("###");
            mask.setPlaceholder("000");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mask;
    }

    @Override
    public void employeeIsPresent() {
        checkEmployeeLate.setEnabled(true);
        lbMinutes.setEnabled(true);
    }

    @Override
    public void employeeIsAbsent() {
        checkEmployeeLate.setEnabled(false);
        checkEmployeeLate.setSelected(false);
        tfMinutesLate.setEnabled(false);
        tfMinutesLate.setText("000");
        lbMinutes.setEnabled(false);
    }

    @Override
    public void employeeAttendanceDataOnSelection(CRUDAttendance.EmployeeAttendanceStatus eas) {
        if (eas.getWasAttendanceTaken()) {
            if (eas.getEmployeeStoredAttendanceState()) {
                // The state was present
                // Enable the checkbox of late
                // Retreive from database if employee was late,
                // if late update the minutes field
                checkEmployeeLate.setEnabled(true);
                lbMinutes.setEnabled(true);
            } else {
                // The state was absent
                checkEmployeeLate.setEnabled(false);
                checkEmployeeLate.setSelected(false);
                tfMinutesLate.setEnabled(false);
                tfMinutesLate.setText("000");
                lbMinutes.setEnabled(false);
            }
        } else {
            // Attendance was NOT taken yet
            checkEmployeeLate.setEnabled(false);
            checkEmployeeLate.setSelected(false);
            tfMinutesLate.setEnabled(false);
            tfMinutesLate.setText("000");
            lbMinutes.setEnabled(false);
        }
    }

    @Override
    public void employeeSelectionCleared() {
        checkEmployeeLate.setEnabled(false);
        checkEmployeeLate.setSelected(false);
        tfMinutesLate.setEnabled(false);
        tfMinutesLate.setText("000");
        lbMinutes.setEnabled(false);
    }

    @Override
    public void dateChanged(CRUDAttendance.EmployeeAttendanceStatus eas) {
        if (eas.getWasAttendanceTaken()) {
            if (eas.getEmployeeStoredAttendanceState()) {
                // The state was present
                // Enable the checkbox of late
                // Retreive from database if employee was late,
                // if late update the minutes field
                checkEmployeeLate.setEnabled(true);
                lbMinutes.setEnabled(true);
            } else {
                // The state was absent
                checkEmployeeLate.setEnabled(false);
                checkEmployeeLate.setSelected(false);
                tfMinutesLate.setEnabled(false);
                tfMinutesLate.setText("000");
                lbMinutes.setEnabled(false);
            }
        } else {
            // Attendance was NOT taken yet
            checkEmployeeLate.setEnabled(false);
            checkEmployeeLate.setSelected(false);
            tfMinutesLate.setEnabled(false);
            tfMinutesLate.setText("000");
            lbMinutes.setEnabled(false);
        }
    }

    private class CheckBoxHandler implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent arg0) {

            int state = arg0.getStateChange();
            if (state == ItemEvent.SELECTED) {
                tfMinutesLate.setEnabled(true);
            } else {
                tfMinutesLate.setEnabled(false);
                tfMinutesLate.setText("000");
            }
        }
    }

}
