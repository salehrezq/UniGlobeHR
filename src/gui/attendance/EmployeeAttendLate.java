/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendance;

import datalink.CRUDAttendance;
import datalink.CRUDLateAttendance;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.Attendance;
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
        SubmitAttendanceListener,
        AttendanceEditModeListener {

    private JCheckBox checkEmployeeLate;
    private JSpinner spinnerMinutesLate;
    private JLabel lbMinutes;
    private ArrayList<LateAttendanceListener> lateAttendanceListeners;

    public EmployeeAttendLate() {

        lateAttendanceListeners = new ArrayList<>();
        checkEmployeeLate = new JCheckBox();
        checkEmployeeLate.setEnabled(false);
        checkEmployeeLate.addActionListener(new CheckBoxHandler());
        SpinnerModel spnModel = new SpinnerNumberModel(1, 1, 60, 1);
        spinnerMinutesLate = new JSpinner(spnModel);
        spinnerMinutesLate.addChangeListener(new MinutesLateSpinnerHandler());
        spinnerMinutesLate.setPreferredSize(new Dimension(45, 20));
        spinnerMinutesLate.setEnabled(false);
        lbMinutes = new JLabel("Minutes");
        lbMinutes.setEnabled(false);

        this.add(checkEmployeeLate);
        this.add(spinnerMinutesLate);
        this.add(lbMinutes);
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
        spinnerMinutesLate.setEnabled(false);
        spinnerMinutesLate.setValue(1);
        lbMinutes.setEnabled(false);
    }

    @Override
    public void employeeAttendanceDataOnSelection(CRUDAttendance.EmployeeAttendanceStatus eas) {
        reactiveControlsOnLateAttendance(eas);
    }

    @Override
    public void dateChanged(CRUDAttendance.EmployeeAttendanceStatus eas) {
        reactiveControlsOnLateAttendance(eas);
    }

    @Override
    public void employeeSelectionCleared() {
        checkEmployeeLate.setEnabled(false);
        checkEmployeeLate.setSelected(false);
        spinnerMinutesLate.setEnabled(false);
        spinnerMinutesLate.setValue(1);
        lbMinutes.setEnabled(false);
    }

    private void reactiveControlsOnLateAttendance(CRUDAttendance.EmployeeAttendanceStatus eas) {

        checkEmployeeLate.setEnabled(false);
        spinnerMinutesLate.setEnabled(false);
        checkEmployeeLate.setSelected(false);
        spinnerMinutesLate.setValue(1);

        if (eas.getWasAttendanceTaken()) {
            if (eas.getEmployeeStoredAttendanceState()) {
                // The state was present
                // Ask database if employee was late
                // if late update the minutes field
                Late lateAttendance = CRUDLateAttendance.getById(eas.getAttendanceId());
                if (lateAttendance != null) {
                    checkEmployeeLate.setSelected(true);
                    spinnerMinutesLate.setValue(lateAttendance.getMinutes_late());
                }
            }
        }
    }

    @Override
    public void attendanceSubmitSucceeded() {
        this.checkEmployeeLate.setEnabled(false);
        this.spinnerMinutesLate.setEnabled(false);
        this.lbMinutes.setEnabled(false);
    }

    @Override
    public void attendanceSubmitFailed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void attendanceEditModeReact(Attendance attendance, Late lateAttendance) {
        if (lateAttendance != null) {
            checkEmployeeLate.setEnabled(true);
            spinnerMinutesLate.setEnabled(true);
        } else {
            checkEmployeeLate.setEnabled(false);
            spinnerMinutesLate.setEnabled(false);
        }
    }

    private class CheckBoxHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {

            JCheckBox checkboxLate = (JCheckBox) arg0.getSource();

            if (checkboxLate.getModel().isSelected()) {
                spinnerMinutesLate.setEnabled(true);
                notifyEmployeeAttendedLate();
            } else {
                spinnerMinutesLate.setEnabled(false);
                notifyemployeeAttendedFine();
                spinnerMinutesLate.setValue(1);
            }
        }
    }

    private class MinutesLateSpinnerHandler implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent arg0) {
            JSpinner s = (JSpinner) arg0.getSource();
            int minutes = (int) s.getValue();
            notifyAttendMinutesLate(minutes);
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
