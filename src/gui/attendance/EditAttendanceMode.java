/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendance;

import datalink.CRUDAttendance;
import datalink.CRUDLateAttendance;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import model.Attendance;
import model.Late;

/**
 *
 * @author Saleh
 */
public class EditAttendanceMode extends JPanel implements
        EmployeeAttendanceDataListener,
        DateChangedAttendanceDataListener,
        SubmitAttendanceListener {

    private final JButton btnEditModeAttendance;
    private CRUDAttendance.EmployeeAttendanceStatus eas;
    private ArrayList<AttendanceEditModeListener> attendanceEditModeListeners;
    private Attendance attendance;
    private Late lateAttendance;

    public EditAttendanceMode() {
        btnEditModeAttendance = new JButton("Edit Mode");
        btnEditModeAttendance.setEnabled(false);
        btnEditModeAttendance.addActionListener(new EditAttendanceModeAction());
        attendanceEditModeListeners = new ArrayList<>();
        this.add(btnEditModeAttendance);
    }

    public void addAttendanceEditModeListener(AttendanceEditModeListener aeml) {
        this.attendanceEditModeListeners.add(aeml);
    }

    private void notifyAttendanceEditMode() {
        this.attendanceEditModeListeners.forEach((aeml) -> {
            aeml.attendanceEditModeReact(attendance, lateAttendance);
        });
    }

    @Override
    public void employeeAttendanceDataOnSelection(CRUDAttendance.EmployeeAttendanceStatus eas) {
        if (eas.getWasAttendanceTaken()) {
            btnEditModeAttendance.setEnabled(true);
            this.eas = eas;
        } else {
            this.eas = null;
            btnEditModeAttendance.setEnabled(false);
        }
    }

    @Override
    public void employeeSelectionCleared() {
        btnEditModeAttendance.setEnabled(false);
    }

    @Override
    public void dateChanged(CRUDAttendance.EmployeeAttendanceStatus eas) {
        if (eas.getWasAttendanceTaken()) {
            btnEditModeAttendance.setEnabled(true);
            this.eas = eas;
        } else {
            this.eas = null;
            btnEditModeAttendance.setEnabled(false);
        }
    }

    @Override
    public void attendanceSubmitSucceeded() {
        btnEditModeAttendance.setEnabled(true);
    }

    @Override
    public void attendanceSubmitFailed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    class EditAttendanceModeAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (eas != null) {
                int attendanceId = eas.getAttendanceId();
                attendance = CRUDAttendance.getById(attendanceId);
                lateAttendance = CRUDLateAttendance.getById(attendanceId);
                btnEditModeAttendance.setEnabled(false);
                notifyAttendanceEditMode();
            }
        }
    }
}
