package gui;

import datalink.CRUDSalary;
import gui.salary.SalaryInput;
import gui.salary.SubjectDateChangeListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import javax.swing.JCheckBoxMenuItem;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class MenuItemSalaryUpToDate
        implements
        EmployeeSelectedListener,
        SubjectDateChangeListener {

    private JCheckBoxMenuItem checkBoxMenuItemSwitchSalaryUpToDate;
    private ArrayList<MenuItemSalaryUpToDateListener> menuItemSalaryUpToDateListeners;
    private MenuItemSalaryUpToDateState optionState;
    private SalaryInput salaryInput;
    private Employee employee;
    private boolean boolSalaryPaid;

    public MenuItemSalaryUpToDate() {
        checkBoxMenuItemSwitchSalaryUpToDate = new JCheckBoxMenuItem("Enable salary up to date option");
        checkBoxMenuItemSwitchSalaryUpToDate.addItemListener(new MenuItemActionHandler());
        menuItemSalaryUpToDateListeners = new ArrayList<>();
        optionState = MenuItemSalaryUpToDateState.DISABLED;
        enableCheckBox(false);
    }

    private enum MenuItemSalaryUpToDateState {
        ENABLED, DISABLED
    };

    @Override
    public void yearOrMonthChanged(YearMonth yearMonth) {
        if (employee != null) {
            LocalDate yearMonthSubjectOfSalary = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
            boolSalaryPaid = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), yearMonthSubjectOfSalary) != null;

            if (boolSalaryPaid) {
                setOptionSelected(false);
                enableCheckBox(false);
            } else {
                enableCheckBox(true);
            }
        }
    }

    @Override
    public void yearAndMonthNotChanged(YearMonth yearMonth) {
        if (employee != null) {
            LocalDate yearMonthSubjectOfSalary = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
            boolSalaryPaid = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), yearMonthSubjectOfSalary) != null;

            if (boolSalaryPaid) {
                setOptionSelected(false);
                enableCheckBox(false);
            } else {
                enableCheckBox(true);
            }
        }
    }

    protected JCheckBoxMenuItem getCheckBoxMenuItemEnableDisableSalaryUpToSelectedDate() {
        return checkBoxMenuItemSwitchSalaryUpToDate;
    }

    public void addMenuItemSalaryUpToDateListener(MenuItemSalaryUpToDateListener misutdml) {
        this.menuItemSalaryUpToDateListeners.add(misutdml);
    }

    private void notifyOptionAbility(boolean enabled) {
        this.menuItemSalaryUpToDateListeners.forEach((misutdml) -> {
            misutdml.salaryUpToDateAbility(enabled);
        });
    }

    private void enableCheckBox(boolean enable) {
        checkBoxMenuItemSwitchSalaryUpToDate.setEnabled(enable);
    }

    protected void setOptionSelected(boolean selected) {
        checkBoxMenuItemSwitchSalaryUpToDate.setSelected(selected);
        notifyOptionAbility(selected);
    }

    @Override
    public void employeeSelected(Employee employee) {
        this.employee = employee;
        if (optionState == MenuItemSalaryUpToDateState.ENABLED) {
            setOptionSelected(false);
        }

        boolSalaryPaid = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), salaryInput.getYearMonthSubjectOfSalary()) != null;

        if (boolSalaryPaid) {
            enableCheckBox(false);
        } else {
            enableCheckBox(true);
        }
    }

    @Override
    public void employeeDeselected() {
        this.employee = null;
        setOptionSelected(false);
        enableCheckBox(false);
    }

    public void setSalaryInput(SalaryInput salaryInput) {
        this.salaryInput = salaryInput;
    }

    private class MenuItemActionHandler implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            boolean isSelected = checkBoxMenuItemSwitchSalaryUpToDate.isSelected();
            if (isSelected) {
                optionState = MenuItemSalaryUpToDateState.ENABLED;
                notifyOptionAbility(true);
            } else if (!isSelected) {
                optionState = MenuItemSalaryUpToDateState.DISABLED;
                notifyOptionAbility(false);
            }
        }
    }
}
