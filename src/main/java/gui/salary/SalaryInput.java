package gui.salary;

import crud.CreateListener;
import crud.DeleteListener;
import crud.UpdateICRPListener;
import crud.UpdateListener;
import datalink.CRUDSalary;
import gui.DateDeselectedListener;
import gui.DateListener;
import gui.DatePicker;
import gui.EmployeeSelectedListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.MaskFormatter;
import model.Employee;
import model.Salary;

/**
 *
 * @author Saleh
 */
public class SalaryInput
        implements
        EmployeeSelectedListener,
        CreateListener,
        UpdateListener,
        UpdateICRPListener,
        DisplayableListener,
        RowSelectedListener,
        EditableListener,
        CancelListener,
        DeleteListener {

    private JPanel mainPanel;
    private JPanel panelMetaInputs, panelYearMonthInputs, panelDateOfTake;
    private YearMonth yearAndMonth;
    private JFormattedTextField tfDateSubject;
    private JComboBox monthsList;
    private final String[] monthsNums;
    private DatePicker dateSalaryGiven;
    private JLabel lbAmount;
    private JTextField tfPayable;
    private Color colorFieldRight;
    private Color colorFieldWrong;
    private Color colorDisabled;
    private boolean boolDateFilled;
    private boolean boolSalaryDisplayMode, boolEditMode, boolCreated, boolUpdated;
    private Salary salary;
    private int salaryId;
    private int salaryOldId;

    public SalaryInput() {

        panelMetaInputs = new JPanel();

        GridBagConstraints c;

        colorFieldRight = new Color(226, 252, 237);
        colorFieldWrong = new Color(254, 225, 214);
        colorDisabled = new Color(105, 105, 105);

        Border borderDateOfSalary = BorderFactory.createTitledBorder("Date of salary");

        panelYearMonthInputs = new JPanel();
        panelYearMonthInputs.setBorder(borderDateOfSalary);

        LocalDate today = LocalDate.now();
        yearAndMonth = YearMonth.of(today.getYear(), today.getMonthValue());

        tfDateSubject = new JFormattedTextField(getMaskFormatter());
        tfDateSubject.setPreferredSize(new Dimension(40, 20));
        panelYearMonthInputs.add(tfDateSubject);

        monthsNums = new String[]{"Jan [1]", "Feb [2]", "Mar [3]", "Apr [4]", "May [5]",
            "Jun [6]", "Jul [7]", "Aug [8]", "Sep [9]", "Oct [10]", "Nov [11]", "Dec [12]"};

        monthsList = new JComboBox<>(monthsNums);
        monthsList.setSelectedIndex(yearAndMonth.getMonthValue() - 1);
        panelYearMonthInputs.add(monthsList);

        panelMetaInputs.add(panelYearMonthInputs);

        Border borderDateOfTake = BorderFactory.createTitledBorder("Date of take");
        panelDateOfTake = new JPanel();
        panelDateOfTake.setBorder(borderDateOfTake);

        dateSalaryGiven = new DatePicker();
        dateSalaryGiven.setTodayAsDefault();
        boolDateFilled = true;
        DateListenerImpli dateListenerImpli = new DateListenerImpli();
        dateSalaryGiven.addDateListener(dateListenerImpli);
        dateSalaryGiven.addDateDeselectedListener(dateListenerImpli);
        panelDateOfTake.add(dateSalaryGiven.getDatePicker());
        panelMetaInputs.add(panelDateOfTake);

        ItemChangeListener comboBoxListener = new ItemChangeListener();

        lbAmount = new JLabel("Payable:");
        panelMetaInputs.add(lbAmount);

        tfPayable = new JTextField();
        tfPayable.setEditable(false);
        tfPayable.setFont(new Font("SansSerif", Font.BOLD, 12));
        tfPayable.setPreferredSize(new Dimension(95, 27));
        panelMetaInputs.add(tfPayable);

        mainPanel = new JPanel(new GridBagLayout());

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 30, 5, 30);
        c.weightx = 1.0;
        c.weighty = 1.0;
        mainPanel.add(panelMetaInputs, c);

        setFieldsEditable(false);
    }

    public JPanel getSalaryInputsPanel() {
        return mainPanel;
    }

    @Override
    public void employeeSelected(Employee employee) {
        tfPayable.setText(null);
        if (!boolSalaryDisplayMode) {
            setFieldsEditable(true);
        }
        if (boolSalaryDisplayMode && employee != null) {
            clearInputFields();
        }
    }

    @Override
    public void employeeDeselected() {
        setFieldsEditable(false);
        tfPayable.setText(null);
    }

    /**
     * Year of which the salary will take effect.
     *
     * @return String represent the year field input
     */
    public int getSubjectYear() {
        return Integer.parseInt(tfDateSubject.getText());
    }

    public LocalDate getDateSalaryGiven() {
        return dateSalaryGiven.getDate();
    }

    public void setTfPayable(String payable) {
        tfPayable.setText(payable);
    }

    public BigDecimal getPayable() {
        return new BigDecimal(tfPayable.getText());
    }

    public boolean getBoolDateFilled() {
        return boolDateFilled;
    }

    protected void clearInputFields() {
        tfDateSubject.setText(String.valueOf(yearAndMonth.getYear()));
        monthsList.setSelectedIndex(yearAndMonth.getMonthValue() - 1);
        dateSalaryGiven.setTodayAsDefault();
        tfPayable.setText(null);
    }

    private void setFieldsEditable(boolean editable) {
        tfDateSubject.setEditable(editable);
        monthsList.setEnabled(editable);
        dateSalaryGiven.setEnabled(editable);
        tfPayable.setForeground(editable ? null : colorDisabled);
    }

    @Override
    public void created() {
        boolCreated = true;
    }

    @Override
    public void updated() {
        boolUpdated = true;
        if (boolSalaryDisplayMode) {
            setFieldsEditable(false);
            setInputFieldsWithSalary(salaryId);
        }
    }

    @Override
    public void updatedICRP() {
        boolUpdated = true;
        if (boolSalaryDisplayMode) {
            setFieldsEditable(false);
        }
    }

    @Override
    public void displayable() {
        boolSalaryDisplayMode = true;
        clearInputFields();
        setFieldsEditable(false);
    }

    @Override
    public void unDisplayable() {
        boolSalaryDisplayMode = false;
        boolEditMode = false;
        clearInputFields();
        setFieldsEditable(true);
    }

    @Override
    public void rowSelectedWithRecordId(int id) {

        salaryId = id;

        if (boolSalaryDisplayMode) {
            setInputFieldsWithSalary(id);
        }
    }

    public void setInputFieldsWithSalary(int id) {

        if (salary == null || salaryOldId != id || boolUpdated) {
            // If salary object is null, or
            // if new Id is not the same as previous stored Id (salaryOldId), or
            // if database entity update submitted
            // then make a new database request.
            boolUpdated = false; // reset submission flag
            salary = CRUDSalary.getById(id);
        }
        // else: otherwise use previously requested salary object
        //

        LocalDate yearMonthSubject = salary.getYearMonthSubject();
        java.time.Year year = Year.from(yearMonthSubject);
        Month month = Month.from(yearMonthSubject);

        tfDateSubject.setText(year.toString());
        monthsList.setSelectedIndex(month.getValue() - 1);
        dateSalaryGiven.setDateValue(salary.getDateGiven());
        tfPayable.setText(String.valueOf(salary.getPayable()));

        // Store id for future comparsions,
        // you find comparsion at the top of this method
        salaryOldId = salary.getId();
    }

    @Override
    public void editable() {
        boolEditMode = true;
        setFieldsEditable(true);
    }

    @Override
    public void cancelled() {
        if (boolSalaryDisplayMode) {
            // Display mode, and posibly edit mode
            boolEditMode = false;
            setFieldsEditable(false);
            setInputFieldsWithSalary(salaryId);
        } else if (!boolSalaryDisplayMode) {
            // Create mode
            int dialogResult = JOptionPane.showConfirmDialog(null,
                    "This will clear content from all input fields\n"
                    + "Are you sure?",
                    "Warning", JOptionPane.YES_OPTION);

            if (dialogResult == JOptionPane.YES_OPTION) {
                this.clearInputFields();
            }
        }
    }

    @Override
    public void deleted() {
        clearInputFields();
    }

    /**
     * Month of which the salary will take effect.
     *
     * @return int represent the month
     */
    public int getSubjectMonth() {
        return monthsList.getSelectedIndex() + 1;
    }

    private class DateListenerImpli implements DateListener, DateDeselectedListener {

        @Override
        public void dateChanged(LocalDate date) {
            boolDateFilled = true;
        }

        @Override
        public void dateDeselected() {
            boolDateFilled = false;
        }
    }

    class ItemChangeListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent event) {

        }
    }

    /**
     * This method returns MaskFormatter that enforces 4 digits The # character
     * represent digit, and four of them (####) means the allowed number of
     * digits.
     *
     * The current year is used as a place holder.
     *
     * @return MaskFormatter
     */
    private MaskFormatter getMaskFormatter() {
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter("####");
            mask.setPlaceholder(String.valueOf(yearAndMonth.getYear()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mask;
    }
}
