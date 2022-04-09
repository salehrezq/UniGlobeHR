package gui.salary;

import datalink.CRUDAttendance;
import gui.EmployeeSelectedListener;
import gui.attendance.AttendanceDeductionsCalculator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.Month;
import java.time.YearMonth;
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
    private Payables payables;

    public Compute() {

        btnCompute = new JButton("Compute");
        btnCompute.addActionListener(new ComputePayables());
    }

    public JButton getBtnCompute() {
        return btnCompute;
    }

    public void setSalaryInput(SalaryInput salaryInput) {
        this.salaryInput = salaryInput;
    }

    public void setPayables(Payables payables) {
        this.payables = payables;
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
    }

    @Override
    public void employeeDeselected() {
        this.employee = null;
    }

    class ComputePayables implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
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

            payables.setTfAttendanceDeductions(attendanceDeductions.toPlainString());
        }
    }

}
