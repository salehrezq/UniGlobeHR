package gui.salary;

import gui.DatePicker;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author Saleh
 */
public class SalaryUpToDayInMonth {

    private JPanel container, panelOptions;
    private JRadioButton rBtnDateToTheEndOfMonth, rBtnDateToSelectedDayInMonth;
    private ButtonGroup btnGroup;
    private DatePicker dateSalaryUpToDay;

    public SalaryUpToDayInMonth() {

        GridBagConstraints c;
        container = new JPanel(new GridBagLayout());

        rBtnDateToTheEndOfMonth = new JRadioButton("To the end of the month");
        c = new GridBagConstraints();
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        container.add(rBtnDateToTheEndOfMonth, c);

        rBtnDateToSelectedDayInMonth = new JRadioButton("Up to selected day");
        c = new GridBagConstraints();
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        container.add(rBtnDateToSelectedDayInMonth, c);

        btnGroup = new ButtonGroup();
        btnGroup.add(rBtnDateToTheEndOfMonth);
        btnGroup.add(rBtnDateToSelectedDayInMonth);

        dateSalaryUpToDay = new DatePicker();
        dateSalaryUpToDay.setTodayAsDefault();
        c = new GridBagConstraints();
        c.gridy = 2;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        container.add(dateSalaryUpToDay.getDatePicker(), c);
    }

    public JPanel getContainer() {
        return this.container;
    }

}
