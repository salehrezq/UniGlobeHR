/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Saleh
 */
public class Late extends AbsentOrLateEntity {

    private int id;
    private int attendance_id;
    private int minutes_late;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(int attendance_id) {
        this.attendance_id = attendance_id;
    }

    public int getMinutes_late() {
        return minutes_late;
    }

    public void setMinutes_late(int minutes_late) {
        this.minutes_late = minutes_late;
    }

}
