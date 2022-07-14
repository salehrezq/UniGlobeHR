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
public class MenuItemsSalaryDeleteMode
        implements
        EmployeeSelectedListener,
        SubjectDateChangeListener {

    private JCheckBoxMenuItem checkBoxMenuItemSwitchSalaryDeleteMode;
    private ArrayList<MenuItemSalaryDeleteModeListener> menuItemSalaryDeleteModeListeners;
    private MenuItemsSalaryDeleteModeState modeState;
    private SalaryInput salaryInput;
    private Employee employee;
    private boolean boolSalaryPaid;

    public MenuItemsSalaryDeleteMode() {
        checkBoxMenuItemSwitchSalaryDeleteMode = new JCheckBoxMenuItem("Enable salary Delete mode");
        checkBoxMenuItemSwitchSalaryDeleteMode.addItemListener(new MenuItemActionHandler());
        menuItemSalaryDeleteModeListeners = new ArrayList<>();
        modeState = MenuItemsSalaryDeleteModeState.DISABLED;
        enableCheckBox(false);
    }

    private enum MenuItemsSalaryDeleteModeState {
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

    protected JCheckBoxMenuItem getCheckBoxMenuItemSwitchSalaryDeleteMode() {
        return checkBoxMenuItemSwitchSalaryDeleteMode;
    }

    public void addMenuItemSalaryDeleteModeListener(MenuItemSalaryDeleteModeListener misdml) {
        this.menuItemSalaryDeleteModeListeners.add(misdml);
    }

    private void notifyModeAbility(boolean enabled) {
        this.menuItemSalaryDeleteModeListeners.forEach((misdml) -> {
            misdml.modeAbility(enabled);
        });
    }

    private void enableCheckBox(boolean enable) {
        checkBoxMenuItemSwitchSalaryDeleteMode.setEnabled(enable);
    }

    protected void setModeSelected(boolean selected) {
        checkBoxMenuItemSwitchSalaryDeleteMode.setSelected(selected);
        notifyModeAbility(selected);
    }

    @Override
    public void employeeSelected(Employee employee) {
        this.employee = employee;
        if (modeState == MenuItemsSalaryDeleteModeState.ENABLED) {
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
            boolean isSelected = checkBoxMenuItemSwitchSalaryDeleteMode.isSelected();
            if (isSelected) {
                modeState = MenuItemsSalaryDeleteModeState.ENABLED;
                notifyModeAbility(true);
            } else if (!isSelected) {
                modeState = MenuItemsSalaryDeleteModeState.DISABLED;
                notifyModeAbility(false);
            }
        }
    }
}
