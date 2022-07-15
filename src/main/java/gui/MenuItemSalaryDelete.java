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
public class MenuItemSalaryDelete
        implements
        EmployeeSelectedListener,
        SubjectDateChangeListener {

    private JCheckBoxMenuItem checkBoxMenuItemSwitchSalaryDelete;
    private ArrayList<MenuItemSalaryDeleteListener> menuItemSalaryDeleteListeners;
    private MenuItemSalaryDeleteState OptionState;
    private SalaryInput salaryInput;
    private Employee employee;
    private boolean boolSalaryPaid;

    public MenuItemSalaryDelete() {
        checkBoxMenuItemSwitchSalaryDelete = new JCheckBoxMenuItem("Enable salary Delete");
        checkBoxMenuItemSwitchSalaryDelete.addItemListener(new MenuItemActionHandler());
        menuItemSalaryDeleteListeners = new ArrayList<>();
        OptionState = MenuItemSalaryDeleteState.DISABLED;
        enableCheckBox(false);
    }

    private enum MenuItemSalaryDeleteState {
        ENABLED, DISABLED
    };

    @Override
    public void yearOrMonthChanged(YearMonth yearMonth) {
        if (employee != null) {
            LocalDate yearMonthSubjectOfSalary = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
            boolSalaryPaid = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), yearMonthSubjectOfSalary) != null;

            if (boolSalaryPaid) {
                enableCheckBox(true);
            } else {
                setOptionSelected(false);
                enableCheckBox(false);

            }
        }
    }

    @Override
    public void yearAndMonthNotChanged(YearMonth yearMonth) {
        if (employee != null) {
            LocalDate yearMonthSubjectOfSalary = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
            boolSalaryPaid = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), yearMonthSubjectOfSalary) != null;

            if (boolSalaryPaid) {
                enableCheckBox(true);
            } else {
                setOptionSelected(false);
                enableCheckBox(false);
            }
        }
    }

    protected JCheckBoxMenuItem getCheckBoxMenuItemSwitchSalaryDelete() {
        return checkBoxMenuItemSwitchSalaryDelete;
    }

    public void addMenuItemSalaryDeleteListener(MenuItemSalaryDeleteListener misdl) {
        this.menuItemSalaryDeleteListeners.add(misdl);
    }

    private void notifyOptionAbility(boolean enabled) {
        this.menuItemSalaryDeleteListeners.forEach((misdl) -> {
            misdl.deleteAbility(enabled);
        });
    }

    private void enableCheckBox(boolean enable) {
        checkBoxMenuItemSwitchSalaryDelete.setEnabled(enable);
    }

    protected void setOptionSelected(boolean selected) {
        checkBoxMenuItemSwitchSalaryDelete.setSelected(selected);
        notifyOptionAbility(selected);
    }

    @Override
    public void employeeSelected(Employee employee) {
        this.employee = employee;
        if (OptionState == MenuItemSalaryDeleteState.ENABLED) {
            setOptionSelected(false);
        }

        boolSalaryPaid = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), salaryInput.getYearMonthSubjectOfSalary()) != null;

        if (boolSalaryPaid) {
            enableCheckBox(true);
        } else {
            enableCheckBox(false);
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
            boolean isSelected = checkBoxMenuItemSwitchSalaryDelete.isSelected();
            if (isSelected) {
                OptionState = MenuItemSalaryDeleteState.ENABLED;
                notifyOptionAbility(true);
            } else if (!isSelected) {
                OptionState = MenuItemSalaryDeleteState.DISABLED;
                notifyOptionAbility(false);
            }
        }
    }
}
