package gui.salary;

import gui.MenuItemSalaryUpToDateModeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Saleh
 */
public class SalaryUpToDayInSubjectMonth
        implements
        SubjectDateChangeListener,
        MenuItemSalaryUpToDateModeListener {

    private JPanel container;
    private JCheckBox cbSalaryUpToDayInSubjectMonth;
    private JSpinner spinnerMonthDays;
    private SpinnerModel spnModel;
    private SalaryInput salaryInput;
    private int dayOfMonth;
    private LocalDate dateSelected;
    private Mode mode;

    public enum Mode {
        END_OF_MONTH, BEFORE_END_OF_MONTH;
    }

    public SalaryUpToDayInSubjectMonth() {

        mode = Mode.END_OF_MONTH;

        container = new JPanel();
        cbSalaryUpToDayInSubjectMonth = new JCheckBox("Salary up to selected day of subject month");
        cbSalaryUpToDayInSubjectMonth.setEnabled(false);
        cbSalaryUpToDayInSubjectMonth.addActionListener(new SalaryUpToDayInSubjectMonthModeHandler());
        container.add(cbSalaryUpToDayInSubjectMonth);

        spinnerMonthDays = new JSpinner();
        spinnerMonthDays.setEnabled(false);
        spinnerMonthDays.addChangeListener(new DayOfSubjectMonthSelectedSpinnerHandler());
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
        dayOfMonth = lastDayOfMonth;
        spnModel = new SpinnerNumberModel(lastDayOfMonth, 1, lastDayOfMonth, 1);
        spinnerMonthDays.setModel(spnModel);
    }

    private void setSpinnerMonthDayslModel(YearMonth yearMonth) {
        LocalDate dateOfSpinner = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth().getValue(), 1);
        setSpinnerMonthDayslModel(dateOfSpinner);
    }

    @Override
    public void yearOrMonthChanged(YearMonth yearMonth) {
        setSpinnerMonthDayslModel(yearMonth);
    }

    @Override
    public void yearAndMonthNotChanged(YearMonth yearMonth) {
        setSpinnerMonthDayslModel(yearMonth);
    }

    protected int getDayOfMonth() {
        return this.dayOfMonth;
    }

    protected Mode getMode() {
        return this.mode;
    }

    @Override
    public void setEnabled(boolean enable) {
        cbSalaryUpToDayInSubjectMonth.setEnabled(enable);
        spinnerMonthDays.setEnabled(enable);
    }

    private class DayOfSubjectMonthSelectedSpinnerHandler implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner spinner = (JSpinner) e.getSource();
            dayOfMonth = (int) spinner.getValue();
        }
    }

    private class SalaryUpToDayInSubjectMonthModeHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            if (checkBox.isSelected()) {
                mode = Mode.BEFORE_END_OF_MONTH;
            } else {
                mode = Mode.END_OF_MONTH;
            }
        }
    }
}
