/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendancedeductions;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.ChronoField;
import java.util.List;

/**
 *
 * @author Saleh
 */
public class AttendanceDeductionsCalculator {

    /**
     * Steps of deductions.
     *
     * First day of month is always single day deduction.
     *
     * subsequent day: - If it comes within the next 5 days from the previous
     * one; deduct 2 days. - If it comes after 5 days from the previous one;
     * deduct 1 day.
     *
     * Fridays are always dropped from calculations; in that if a subsequent day
     * comes after 6 days from the previous one, and one day of these 6 days is
     * a Friday; 1 is dropped from the 6 days which accounts for the Friday, and
     * in this case the subsequent day is assumed to be after 5 days because of
     * omitting the Friday.
     *
     * @param listOfAbsentDays
     * @param ym
     */
    public static void calculateDeductions(List<Object[]> listOfAbsentDays, YearMonth ym) {

        int size = listOfAbsentDays.size();

        for (int i = 0; i < size; i++) {

            if (size == 1) {
                System.out.println("only day single deduction");
                break;
            }

            if (size > 1 && i == 0) {
                System.out.println("first day - single day deduction");
            }

            int day = (int) listOfAbsentDays.get(i)[1];
            int nextDay = -1;
            if ((i + 1) < size) {
                nextDay = (int) listOfAbsentDays.get(i + 1)[1];
            }
            // If no more days found.
            if (nextDay == -1) {
                break;
            }

            // If Friday exists within then next 6 days from day, then omit the Friday
            // asume it is 6 days and omit the Friday.
            int diff = nextDay - day;
            int fridayDedected = 0;
            if (diff == 6) {
                fridayDedected = dedectFridayBetweenTwoDaysOfMonth(day, nextDay, ym);
            }

            if (((nextDay - fridayDedected) - day) < 6) {
                System.out.print("double deduction");
                System.out.print((fridayDedected > 0) ? " Friday omited" : "");
                System.out.println();
            } else {
                System.out.println("single day deduction");
            }
        }
    }

    /**
     * Check if Friday exist within 7 days. Works for two days at the same
     * month.
     *
     * @param day
     * @param nextDay
     * @param ym
     * @return
     */
    private static int dedectFridayBetweenTwoDaysOfMonth(int day, int nextDay, YearMonth ym) {

        int fridayDedected = 0;
        int diff = nextDay - day;

        LocalDate friday;
        int startDay = day + 1;
        int endDay = startDay + (diff - 1);
        for (int i = startDay; i < endDay; i++) {
            friday = LocalDate.of(ym.getYear(), ym.getMonth(), i);
            // Friday is 5
            if (friday.get(ChronoField.DAY_OF_WEEK) == 5) {
                fridayDedected = +1;
            }
        }
        return fridayDedected;
    }

}
