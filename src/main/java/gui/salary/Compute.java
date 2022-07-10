package gui.salary;

import datalink.CRUDAttendance;
import datalink.CRUDPerformance;
import datalink.CRUDSalary;
import datalink.CRUDSalaryAdvance;
import gui.EmployeeSelectedListener;
import gui.MenuItemSalaryUpToDateModeListener;
import gui.attendance.AttendanceDeductionsCalculator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import model.AbsentOrLateEntity;
import model.AttendanceDeduction;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class Compute
        implements
        EmployeeSelectedListener,
        MenuItemSalaryUpToDateModeListener,
        SalaryUpToDateSpinnerCheckedListener,
        SubjectDateChangeListener {

    private JButton btnCompute;
    private Employee employee;
    private SalaryInput salaryInput;
    private Details details;
    private List<ComputeListener> computeListeners;
    private int yearSubjectOldValue;
    private int monthSubjectOldValue;
    private ArrayList<PaymnetListener> paymnetListeners;
    private SalaryUpToDayInSubjectMonth salaryUpToDayInSubjectMonth;
    private boolean boolSalaryPaid, boolMenuItemOfSalaryUpToDateMode, boolSpinnerChecked;

    public Compute() {

        btnCompute = new JButton("Compute");
        btnCompute.setEnabled(false);
        btnCompute.addActionListener(new ComputePayables());

        computeListeners = new ArrayList<>();
        paymnetListeners = new ArrayList<>();
    }

    public JButton getBtnCompute() {
        return btnCompute;
    }

    public void setSalaryInput(SalaryInput salaryInput) {
        this.salaryInput = salaryInput;
        yearSubjectOldValue = this.salaryInput.getSubjectYear();
        monthSubjectOldValue = this.salaryInput.getSubjectMonth();
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public int getYearSubjectOldValue() {
        return yearSubjectOldValue;
    }

    public int getMonthSubjectOldValue() {
        return monthSubjectOldValue;
    }

    public void setSalaryUpToDayInSubjectMonth(SalaryUpToDayInSubjectMonth salaryUpToDayInSubjectMonth) {
        this.salaryUpToDayInSubjectMonth = salaryUpToDayInSubjectMonth;
    }

    public static BigDecimal getAttendanceDeductionsByEmployeeOfMonth(int employeeId, YearMonth ym) {
        List<AbsentOrLateEntity> listOfAbsentAndLateDays = CRUDAttendance.getAbsenceAndLatesRecordByEmployeeByMonth(employeeId, ym);
        List<AttendanceDeduction> attendanceDeductionsList
                = AttendanceDeductionsCalculator
                        .getAbsentsAndLatesDeductions(listOfAbsentAndLateDays, ym);

        BigDecimal result
                = attendanceDeductionsList.stream()
                        .map(AttendanceDeduction::getDeduction)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return result;
    }

    @Override
    public void employeeSelected(Employee employee) {
        this.employee = employee;
        boolSalaryPaid = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), salaryInput.getYearMonthSubjectOfSalary()) != null;

        if (boolSalaryPaid) {
            btnCompute.setEnabled(false);
        } else {
            btnCompute.setEnabled(true);
        }
    }

    @Override
    public void employeeDeselected() {
        this.employee = null;
        boolSalaryPaid = false;
        btnCompute.setEnabled(false);
    }

    @Override
    public void modeAbility(boolean enable) {
        boolMenuItemOfSalaryUpToDateMode = enable;
        if (employee != null) {
            if (enable) {
                btnCompute.setEnabled(false);
            } else if (!enable && !boolSalaryPaid) {
                btnCompute.setEnabled(true);
            }
        }
    }

    @Override
    public void spinnerChecked(boolean checked) {
        boolSpinnerChecked = checked;
        btnCompute.setEnabled(checked);
    }

    @Override
    public void yearOrMonthChanged(YearMonth yearMonth) {
        if (employee != null) {
            LocalDate yearMonthSubjectOfSalary = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
            boolSalaryPaid = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), yearMonthSubjectOfSalary) != null;

            if (boolSalaryPaid) {
                btnCompute.setEnabled(false);
            } else {
                if (boolMenuItemOfSalaryUpToDateMode && !boolSpinnerChecked) {
                    btnCompute.setEnabled(false);
                } else {
                    btnCompute.setEnabled(true);
                }
            }
        }
    }

    @Override
    public void yearAndMonthNotChanged(YearMonth yearMonth) {
        if (employee != null) {
            LocalDate yearMonthSubjectOfSalary = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
            boolSalaryPaid = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), yearMonthSubjectOfSalary) != null;

            if (boolSalaryPaid) {
                btnCompute.setEnabled(false);
            } else {
                if (boolMenuItemOfSalaryUpToDateMode && !boolSpinnerChecked) {
                    btnCompute.setEnabled(false);
                } else {
                    btnCompute.setEnabled(true);
                }
            }
        }
    }

    class ComputePayables implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            yearSubjectOldValue = salaryInput.getSubjectYear();
            monthSubjectOldValue = salaryInput.getSubjectMonth();

            // In different places of the code; checks made to check if salary has
            // been paid. The check always disable the btnCompute if salary has been paid
            // otherwise enable it; the remaining case of salary is pending, so here
            // we notify regarding it.
            notifyPaymentPending();

            if (employee == null) {
                JOptionPane.showConfirmDialog(null,
                        "Select Employee", "",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }

            int year = salaryInput.getSubjectYear();
            Month month = Month.of(salaryInput.getSubjectMonth());
            YearMonth ym = YearMonth.of(year, month);
            BigDecimal attendanceDeductions = getAttendanceDeductionsByEmployeeOfMonth(employee.getId(), ym);

            details.setTfAttendanceDeductions(attendanceDeductions.toPlainString());

            BigDecimal salaryAdvancesAggregated
                    = CRUDSalaryAdvance.getSalaryAdvancesRecordByEmployeeByMonthAggregated(employee.getId(), ym);
            details.setTfSalaryAdvances(salaryAdvancesAggregated.toPlainString());

            BigDecimal performanceGain = CRUDPerformance.getPerformanceRecordByEmployeeByMonthAggregated(employee.getId(), ym);
            details.setTfPerformanceGain(performanceGain.toPlainString());

            LocalDate emplyeeEnrollmentDate = employee.getEnrolledDate();
            int yearOfEmplyeeEnrollmentDate = emplyeeEnrollmentDate.getYear();
            int monthOfEmplyeeEnrollmentDate = emplyeeEnrollmentDate.getMonthValue();
            int yearOfSalaryEndDate = salaryInput.getSubjectYear();
            int monthOfSalaryEndDate = salaryInput.getSubjectMonth();
            SalaryUpToDayInSubjectMonth.Mode mode = salaryUpToDayInSubjectMonth.getMode();

            boolean areTwoDatesInTheSameYearMonth
                    = (yearOfEmplyeeEnrollmentDate == yearOfSalaryEndDate)
                    && (monthOfEmplyeeEnrollmentDate == monthOfSalaryEndDate);

            BigDecimal payableAmount = BigDecimal.ZERO;

            if (mode == SalaryUpToDayInSubjectMonth.Mode.END_OF_MONTH && !areTwoDatesInTheSameYearMonth) {
                // Case A: usual employee
                // If mode is END_OF_MONTH 
                // and the year and month are not the same for both;
                // the employee enrollment date and salary subject date
                // then it is full month salary.
                payableAmount = employee.getSalary()
                        .subtract(salaryAdvancesAggregated)
                        .subtract(attendanceDeductions)
                        .add(performanceGain);
                System.out.println("Normal full month");
            } else if ((mode == SalaryUpToDayInSubjectMonth.Mode.BEFORE_END_OF_MONTH) && (!areTwoDatesInTheSameYearMonth)) {
                // Case B: experienced leaving employee
                // If mode is BEFORE_END_OF_MONTH
                // and the year and month are not the same for both;
                // the employee enrollment date and salary subject date
                // then salary calculated from the begning of the subject month date
                // up to the selected day of the subject month.
                int upToDay = salaryUpToDayInSubjectMonth.getDayOfMonth();
                payableAmount = employee.salaryOfSubMonth(upToDay)
                        .subtract(salaryAdvancesAggregated)
                        .subtract(attendanceDeductions)
                        .add(performanceGain);
                System.out.println("Termination after more than one month jump");
            } else if (mode == SalaryUpToDayInSubjectMonth.Mode.BEFORE_END_OF_MONTH && areTwoDatesInTheSameYearMonth) {
                // Case C short lived employee
                // If mode is BEFORE_END_OF_MONTH
                // and the year and month are the same for both;
                // the employee enrollment date and salary subject date
                // then it is short lived employee
                // Get difference in days between leaving date and enrollment date,
                // and calculate salary for those days.
                int days = salaryUpToDayInSubjectMonth.getDayOfMonth() - emplyeeEnrollmentDate.getDayOfMonth();
                payableAmount = employee.salaryOfSubMonth(days + 1)
                        .subtract(salaryAdvancesAggregated)
                        .subtract(attendanceDeductions)
                        .add(performanceGain);
                System.out.println("Termination of short lived");
            } else if (mode == SalaryUpToDayInSubjectMonth.Mode.END_OF_MONTH && areTwoDatesInTheSameYearMonth) {
                // Case D fresh employee
                // His first month of work that does
                // not start from the begning of the month.
                // Calculate salary from enrollment day date up to the end of the month.
                LocalDate maximumDayDateOfSubjectMonth = salaryInput.getYearMonthSubjectOfSalary().with(TemporalAdjusters.lastDayOfMonth());
                int lastDayOfSubjectMonth = maximumDayDateOfSubjectMonth.getDayOfMonth();
                int enrollmentDay = emplyeeEnrollmentDate.getDayOfMonth();
                int days = lastDayOfSubjectMonth - enrollmentDay;
                payableAmount = employee.salaryOfSubMonth(days + 1)
                        .subtract(salaryAdvancesAggregated)
                        .subtract(attendanceDeductions)
                        .add(performanceGain);
                System.out.println("Normal fresh month");
            }
            System.out.println("payableAmount " + payableAmount);
            notifyComputed(payableAmount);
        }
    }

    public void addComputeListener(ComputeListener cl) {
        this.computeListeners.add(cl);
    }

    private void notifyComputed(BigDecimal amount) {
        this.computeListeners.forEach((cl) -> {
            cl.computed(amount);
        });
    }

    public void addPaymnetListener(PaymnetListener paymentL) {
        this.paymnetListeners.add(paymentL);
    }

    private void notifyPaymentCleared() {
        this.paymnetListeners.forEach((paymentL) -> {
            paymentL.cleared();
        });
    }

    private void notifyPaymentPending() {
        this.paymnetListeners.forEach((paymentL) -> {
            paymentL.pending();
        });
    }

}
