/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gui.attendance;

import model.Attendance;
import model.Late;

/**
 *
 * @author Saleh
 */
public interface SubmittedAttendanceEntitiesListener {

    public void submittedAttendanceEntities(Attendance submittedAttendanceEntity, Late submittedLateAttendanceEntity);
}
