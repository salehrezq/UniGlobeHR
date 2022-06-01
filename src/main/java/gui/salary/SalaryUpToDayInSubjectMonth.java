package gui.salary;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Saleh
 */
public class SalaryUpToDayInSubjectMonth {

    private JPanel container;
    private JCheckBox cbSalaryUpToDayInSubjectMonth;
    private JSpinner spinnerMonthDays;
    private SpinnerModel spnModel;
    private SalaryInput salaryInput;

    public SalaryUpToDayInSubjectMonth() {

        container = new JPanel();
        cbSalaryUpToDayInSubjectMonth = new JCheckBox("Salary up to selected day of subject month");
        container.add(cbSalaryUpToDayInSubjectMonth);

        spinnerMonthDays = new JSpinner();
        container.add(spinnerMonthDays);
    }

    public JPanel getContainer() {
        return this.container;
    }

    public void setSalaryInput(SalaryInput salaryInput) {
        this.salaryInput = salaryInput;
    }

    public void setSpinnerMonthDayslModel(LocalDate subjectYearMonth) {
        LocalDate lastDay = subjectYearMonth.with(TemporalAdjusters.lastDayOfMonth()); //2015-11-30
        int lastDayOfMonth = lastDay.getDayOfMonth();
        spnModel = new SpinnerNumberModel(lastDayOfMonth, 1, lastDayOfMonth, 1);
        spinnerMonthDays.setModel(spnModel);
    }
}
