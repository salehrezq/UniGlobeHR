/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendancedeductions;

import java.util.List;

/**
 *
 * @author Saleh
 */
public class AttendanceDeductionsCalculator {

    public static void calculateDeductions(List<Object[]> attendance) {

        attendance.forEach((item) -> {
            System.out.println(item[0]);
            System.out.println(item[1]);
        });

    }

}
