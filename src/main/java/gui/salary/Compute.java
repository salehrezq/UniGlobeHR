package gui.salary;

import datalink.CRUDAttendance;
import datalink.CRUDPerformance;
import datalink.CRUDSalary;
import datalink.CRUDSalaryAdvance;
import gui.EmployeeSelectedListener;
import gui.attendance.AttendanceDeductionsCalculator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.Month;
import java.time.YearMonth;
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
        EmployeeSelectedListener {

    private JButton btnCompute;
    private Employee employee;
    private SalaryInput salaryInput;
    private Details details;
    private List<ComputeListener> computeListeners;
    private int yearSubjectOldValue;
    private int monthSubjectOldValue;
    private ArrayList<PaymnetListener> paymnetListeners;

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
        btnCompute.setEnabled(true);
    }

    @Override
    public void employeeDeselected() {
        this.employee = null;
        btnCompute.setEnabled(false);
    }

    class ComputePayables implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            yearSubjectOldValue = salaryInput.getSubjectYear();
            monthSubjectOldValue = salaryInput.getSubjectMonth();

            if (CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(),
                    salaryInput.getYearMonthSubjectOfSalary())) {
                notifyPaymentCleared();
            } else {
                notifyPaymentPending();
            }

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

            BigDecimal payable = employee.getSalary()
                    .subtract(salaryAdvancesAggregated)
                    .subtract(attendanceDeductions)
                    .add(performanceGain);

            salaryInput.setTfPayable(payable.toPlainString());

            notifyComputed();
        }
    }

    public void addComputeListener(ComputeListener cl) {
        this.computeListeners.add(cl);
    }

    private void notifyComputed() {
        this.computeListeners.forEach((cl) -> {
            cl.computed();
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
