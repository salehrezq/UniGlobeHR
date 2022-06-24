package gui;

import datalink.CRUDSalary;
import gui.salary.SalaryInput;
import gui.salary.SubjectDateChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import model.Employee;

/**
 *
 * @author Saleh
 */
public class MenuItemsSalaryUpToDateMode
        implements
        EmployeeSelectedListener,
        SubjectDateChangeListener {

    private JMenuItem itemActionEnableSalaryUpToSelectedDate,
            itemActionDisableSalaryUpToSelectedDate;
    private static final String STR_ENABLE_SALARY_UPTO_SELECTED_DATE = "Enable salary up to date";
    private static final String STR_DISABLE_ALARY_UPTO_SELECTED_DATE = "Disable salary up to date";
    private static final String BULLET_SELECTED = "⚫";
    private static final String BULLET_DESELECTED = "⚪";
    private ArrayList<MenuItemSalaryUpToDateModeListener> menuItemSalaryUpToDateModeListeners;
    private MenuItemsSalaryUpToDateModeState modeState;
    private SalaryInput salaryInput;
    private Employee employee;
    private boolean boolSalaryPaid;

    public MenuItemsSalaryUpToDateMode() {
        MenuItemActions menuItemActions = new MenuItemActions();
        itemActionEnableSalaryUpToSelectedDate = new JMenuItem(BULLET_DESELECTED + " " + STR_ENABLE_SALARY_UPTO_SELECTED_DATE);
        itemActionEnableSalaryUpToSelectedDate.addActionListener(menuItemActions);
        itemActionDisableSalaryUpToSelectedDate = new JMenuItem(BULLET_SELECTED + " " + STR_DISABLE_ALARY_UPTO_SELECTED_DATE);
        itemActionDisableSalaryUpToSelectedDate.addActionListener(menuItemActions);
        menuItemSalaryUpToDateModeListeners = new ArrayList<>();
        modeState = MenuItemsSalaryUpToDateModeState.DISABLED;
        enableControls(false);
    }

    @Override
    public void yearOrMonthChanged(YearMonth yearMonth) {
        LocalDate yearMonthSubjectOfSalary = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
        boolSalaryPaid = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), yearMonthSubjectOfSalary) != null;

        if (boolSalaryPaid) {
            itemActionDisableSalaryUpToSelectedDate.doClick();
            enableControls(false);
        } else {
            enableControls(true);
        }
    }

    @Override
    public void yearAndMonthNotChanged(YearMonth yearMonth) {
        LocalDate yearMonthSubjectOfSalary = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
        boolSalaryPaid = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), yearMonthSubjectOfSalary) != null;

        if (boolSalaryPaid) {
            itemActionDisableSalaryUpToSelectedDate.doClick();
            enableControls(false);
        } else {
            enableControls(true);
        }
    }

    private enum MenuItemsSalaryUpToDateModeState {
        ENABLED, DISABLED
    };

    protected JMenuItem getItemActionEnableSalaryUpToSelectedDate() {
        return itemActionEnableSalaryUpToSelectedDate;
    }

    protected JMenuItem getItemActionDisableSalaryUpToSelectedDate() {
        return itemActionDisableSalaryUpToSelectedDate;
    }

    public void addMenuItemSalaryUpToDateModeListener(MenuItemSalaryUpToDateModeListener misutdml) {
        this.menuItemSalaryUpToDateModeListeners.add(misutdml);
    }

    private void notifyModeAbility(boolean enabled) {
        this.menuItemSalaryUpToDateModeListeners.forEach((misutdml) -> {
            misutdml.modeAbility(enabled);
        });
    }

    private void enableControls(boolean enable) {
        itemActionEnableSalaryUpToSelectedDate.setEnabled(enable);
        itemActionDisableSalaryUpToSelectedDate.setEnabled(enable);
    }

    @Override
    public void employeeSelected(Employee employee) {
        this.employee = employee;
        if (modeState == MenuItemsSalaryUpToDateModeState.ENABLED) {
            itemActionDisableSalaryUpToSelectedDate.doClick();
        }

        boolSalaryPaid = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), salaryInput.getYearMonthSubjectOfSalary()) != null;

        if (boolSalaryPaid) {
            enableControls(false);
        } else {
            enableControls(true);
        }
    }

    @Override
    public void employeeDeselected() {
        this.employee = null;
        if (modeState == MenuItemsSalaryUpToDateModeState.DISABLED) {
            itemActionDisableSalaryUpToSelectedDate.doClick();
        }
        enableControls(false);
    }

    public void setSalaryInput(SalaryInput salaryInput) {
        this.salaryInput = salaryInput;
    }

    private class MenuItemActions implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            if (source == itemActionEnableSalaryUpToSelectedDate) {
                itemActionEnableSalaryUpToSelectedDate.setText(BULLET_SELECTED + " " + STR_ENABLE_SALARY_UPTO_SELECTED_DATE);
                itemActionDisableSalaryUpToSelectedDate.setText(BULLET_DESELECTED + " " + STR_DISABLE_ALARY_UPTO_SELECTED_DATE);
                modeState = MenuItemsSalaryUpToDateModeState.ENABLED;
                notifyModeAbility(true);
            } else if (source == itemActionDisableSalaryUpToSelectedDate) {
                itemActionEnableSalaryUpToSelectedDate.setText(BULLET_DESELECTED + " " + STR_ENABLE_SALARY_UPTO_SELECTED_DATE);
                itemActionDisableSalaryUpToSelectedDate.setText(BULLET_SELECTED + " " + STR_DISABLE_ALARY_UPTO_SELECTED_DATE);
                modeState = MenuItemsSalaryUpToDateModeState.DISABLED;
                notifyModeAbility(false);
            }
        }
    }
}
