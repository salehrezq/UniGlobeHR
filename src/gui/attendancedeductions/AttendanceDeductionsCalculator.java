/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendancedeductions;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 *
 * @author Saleh
 */
public class AttendanceDeductionsCalculator {

    public static void calculateDeductions(List<Object[]> attendance, YearMonth ym) {

        int size = attendance.size();

        for (int i = 0; i < size; i++) {

            if (size == 1) {
                System.out.println("only day single deduction");
                break;
            }

            if (size > 1 && i == 0) {
                System.out.println("first day - single day deduction");
            }

            int day = (int) attendance.get(i)[1];
            int nextDay = -1;
            if ((i + 1) < size) {
                nextDay = (int) attendance.get(i + 1)[1];
            }
            // If no more days found.
            if (nextDay == -1) {
                break;
            }
            if ((nextDay - day) < 6) {
                System.out.println("double deduction");
            } else {
                System.out.println("single day deduction");
            }
        }
    }
}
