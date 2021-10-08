/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendance;

import datalink.CRUDAttendance;

/**
 *
 * @author Saleh
 */
public interface DateChangedAttendanceDataListener {

    public void dateChanged(CRUDAttendance.EmployeeAttendanceStatus eas);
}
