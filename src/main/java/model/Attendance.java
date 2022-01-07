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
public class Attendance extends AbsentOrLateEntity {

    private int id;
    private boolean state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getStateOfAttendance() {
        return state;
    }

    public void setStateOfAttendance(boolean state) {
        this.state = state;
    }

}
