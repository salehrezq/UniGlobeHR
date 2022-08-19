/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendance;

import datalink.CRUDAttendance;
import datalink.CRUDLateAttendance;
import gui.DateDeselectedListener;
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
        SubmitAttendanceListener,
        SubmittedAttendanceEntitiesListener,
        DateDeselectedListener {

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

    private void notifyAttendanceEditMode(Attendance attendance, Late lateAttendance) {
        this.attendanceEditModeListeners.forEach((aeml) -> {
            aeml.attendanceEditModeReact(attendance, lateAttendance);
        });
    }

    @Override
    public void employeeAttendanceDataOnSelection(CRUDAttendance.EmployeeAttendanceStatus eas) {
        if (eas.getWasAttendanceTaken()) {
            // If attendance record was locked, the input will be to setEnabled(false),
            // If attendance record was not locked, the input will be to setEnabled(true),
            btnEditModeAttendance.setEnabled(!eas.isAttendanceRecordLocked());
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
            // If attendance record was locked, the input will be to setEnabled(false),
            // If attendance record was not locked, the input will be to setEnabled(true),
            btnEditModeAttendance.setEnabled(!eas.isAttendanceRecordLocked());
            this.eas = eas;
        } else {
            this.eas = null;
            btnEditModeAttendance.setEnabled(false);
        }
    }

    @Override
    public void dateDeselected() {
        this.btnEditModeAttendance.setEnabled(false);
    }

    @Override
    public void attendanceSubmitSucceeded() {
        btnEditModeAttendance.setEnabled(true);
    }

    @Override
    public void attendanceNoChange() {
        // Same behavior as of attendanceSubmitSucceeded()
        // because no special reaction for no change case.
        attendanceSubmitSucceeded();
    }

    @Override
    public void attendanceSubmitFailed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Receives newly submitted entities to the database.
     *
     * @param submittedAttendanceEntity
     * @param submittedLateAttendanceEntity
     */
    @Override
    public void submittedAttendanceEntities(Attendance submittedAttendanceEntity, Late submittedLateAttendanceEntity) {
        this.attendance = submittedAttendanceEntity;
        this.lateAttendance = submittedLateAttendanceEntity;
    }

    class EditAttendanceModeAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (eas != null) {
                // This branch is reached after employeeAttendanceDataOnSelection() is called
                // and also subsequent dateChanged() calls;
                // which is the case when we select stored employee with specific date.
                int attendanceId = eas.getAttendanceId();
                attendance = CRUDAttendance.getById(attendanceId);
                lateAttendance = CRUDLateAttendance.getById(attendanceId);
                btnEditModeAttendance.setEnabled(false);
                notifyAttendanceEditMode(attendance, lateAttendance);
            } else if ((attendance != null) || (lateAttendance != null)) {
                // This branch is reached after submittedAttendanceEntities() is called,
                // which is the case after fresh submitted records.
                notifyAttendanceEditMode(attendance, lateAttendance);
            }
        }
    }
}
