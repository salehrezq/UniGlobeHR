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
public class MenuItemsSalaryUpToDateMode
        implements
        EmployeeSelectedListener,
        SubjectDateChangeListener {

    private JCheckBoxMenuItem checkBoxMenuItemSwitchSalaryUpToDateMode;
    private ArrayList<MenuItemSalaryUpToDateModeListener> menuItemSalaryUpToDateModeListeners;
    private MenuItemsSalaryUpToDateModeState modeState;
    private SalaryInput salaryInput;
    private Employee employee;
    private boolean boolSalaryPaid;

    public MenuItemsSalaryUpToDateMode() {
        checkBoxMenuItemSwitchSalaryUpToDateMode = new JCheckBoxMenuItem("Enable salary up to date mode");
        checkBoxMenuItemSwitchSalaryUpToDateMode.addItemListener(new MenuItemActionHandler());
        menuItemSalaryUpToDateModeListeners = new ArrayList<>();
        modeState = MenuItemsSalaryUpToDateModeState.DISABLED;
        enableCheckBox(false);
    }

    private enum MenuItemsSalaryUpToDateModeState {
        ENABLED, DISABLED
    };

    @Override
    public void yearOrMonthChanged(YearMonth yearMonth) {
        if (employee != null) {
            LocalDate yearMonthSubjectOfSalary = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
            boolSalaryPaid = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), yearMonthSubjectOfSalary) != null;

            if (boolSalaryPaid) {
                setModeSelected(false);
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
                setModeSelected(false);
                enableCheckBox(false);
            } else {
                enableCheckBox(true);
            }
        }
    }

    protected JCheckBoxMenuItem getCheckBoxMenuItemEnableDisableSalaryUpToSelectedDate() {
        return checkBoxMenuItemSwitchSalaryUpToDateMode;
    }

    public void addMenuItemSalaryUpToDateModeListener(MenuItemSalaryUpToDateModeListener misutdml) {
        this.menuItemSalaryUpToDateModeListeners.add(misutdml);
    }

    private void notifyModeAbility(boolean enabled) {
        this.menuItemSalaryUpToDateModeListeners.forEach((misutdml) -> {
            misutdml.modeAbility(enabled);
        });
    }

    private void enableCheckBox(boolean enable) {
        checkBoxMenuItemSwitchSalaryUpToDateMode.setEnabled(enable);
    }

    protected void setModeSelected(boolean selected) {
        checkBoxMenuItemSwitchSalaryUpToDateMode.setSelected(selected);
        notifyModeAbility(selected);
    }

    @Override
    public void employeeSelected(Employee employee) {
        this.employee = employee;
        if (modeState == MenuItemsSalaryUpToDateModeState.ENABLED) {
            setModeSelected(false);
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
        setModeSelected(false);
        enableCheckBox(false);
    }

    public void setSalaryInput(SalaryInput salaryInput) {
        this.salaryInput = salaryInput;
    }

    private class MenuItemActionHandler implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            boolean isSelected = checkBoxMenuItemSwitchSalaryUpToDateMode.isSelected();
            if (isSelected) {
                modeState = MenuItemsSalaryUpToDateModeState.ENABLED;
                notifyModeAbility(true);
            } else if (!isSelected) {
                modeState = MenuItemsSalaryUpToDateModeState.DISABLED;
                notifyModeAbility(false);
            }
        }
    }
}
