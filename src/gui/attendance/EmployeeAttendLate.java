/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendance;

import datalink.CRUDAttendance;
import datalink.CRUDLateAttendance;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;
import model.Late;

/**
 *
 * @author Saleh
 */
public class EmployeeAttendLate extends JPanel
        implements
        EmployeeAttendanceListener,
        EmployeeAttendanceDataListener,
        DateChangedAttendanceDataListener,
        SubmitAttendanceListener {

    private JCheckBox checkEmployeeLate;
    private JFormattedTextField tfMinutesLate;
    private JLabel lbMinutes;
    private ArrayList<LateAttendanceListener> lateAttendanceListeners;

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
                Late lateAttendance = CRUDLateAttendance.getLateAttendance(eas.getAttendanceId());
                if (lateAttendance != null) {
                    checkEmployeeLate.setSelected(true);
                    tfMinutesLate.setText(String.valueOf(lateAttendance.getMinutes_late()));
                } else {
                    checkEmployeeLate.setSelected(false);
                    tfMinutesLate.setText("000");
                }

                // Ask database if if was late
                // The state was present
                // Enable the checkbox of late
                // Retreive from database if employee was late,
                // if late update the minutes field
            } else {
                // The state was absent
            }
        } else {
            // Attendance was NOT taken yet
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
            } else {
                // The state was absent
            }
        } else {
            // Attendance was NOT taken yet
        }
    }

    @Override
    public void attendanceSubmitSucceeded() {
        this.checkEmployeeLate.setEnabled(false);
        this.tfMinutesLate.setEnabled(false);
        this.lbMinutes.setEnabled(false);
    }

    @Override
    public void attendanceSubmitFailed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class CheckBoxHandler implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent arg0) {

            int state = arg0.getStateChange();
            if (state == ItemEvent.SELECTED) {
                tfMinutesLate.setEnabled(true);
                notifyEmployeeAttendedLate();
            } else {
                tfMinutesLate.setEnabled(false);
                notifyemployeeAttendedFine();
                tfMinutesLate.setText("000");
            }
        }
    }

    public void addLateAttendanceListener(LateAttendanceListener lateal) {
        this.lateAttendanceListeners.add(lateal);
    }

    private void notifyEmployeeAttendedLate() {
        this.lateAttendanceListeners.forEach((lateal) -> {
            lateal.employeeAttendedLate();
        });
    }

    private void notifyAttendMinutesLate(int minutesLate) {
        this.lateAttendanceListeners.forEach((lateal) -> {
            lateal.attendMinutesLate(minutesLate);
        });
    }

    private void notifyemployeeAttendedFine() {
        this.lateAttendanceListeners.forEach((lateal) -> {
            lateal.employeeAttendedFine();
        });
    }
}
