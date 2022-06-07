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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
        DeleteListener,
        ComputeListener {

    private JPanel mainPanel;
    private JPanel panelMetaInputs, panelYearMonthInputs, panelDateOfTake;
    private YearMonth yearAndMonthSubjectInitial;
    private YearMonth yearAndMonthSubjectUpdatable;
    private int yearSubject;
    private int monthSubject;
    private JFormattedTextField tfYearSubject;
    private JComboBox monthsList;
    private final String[] monthsNums;
    private DatePicker dateOfPayment;
    private Color colorFieldRight;
    private Color colorFieldWrong;
    private Color colorDisabled;
    private boolean boolDateFilled;
    private boolean boolSalaryDisplayMode, boolEditMode,
            boolCreated, boolUpdated, boolYearSubjectChanged, boolMonthSubjectChanged;
    private Salary salary;
    private int salaryId;
    private int salaryOldId;
    private Compute compute;
    private ArrayList<SubjectDateChangeListener> subjectDateChangeListeners;

    public SalaryInput() {

        subjectDateChangeListeners = new ArrayList<>();
        panelMetaInputs = new JPanel();

        GridBagConstraints c;

        colorFieldRight = new Color(226, 252, 237);
        colorFieldWrong = new Color(254, 225, 214);
        colorDisabled = new Color(105, 105, 105);

        panelYearMonthInputs = new JPanel();
        Border borderSubjectDate = BorderFactory.createTitledBorder("Subject date");
        panelYearMonthInputs.setBorder(borderSubjectDate);

        LocalDate today = LocalDate.now();
        yearAndMonthSubjectInitial = YearMonth.of(today.getYear(), today.getMonthValue());
        yearAndMonthSubjectUpdatable = yearAndMonthSubjectInitial;
        yearSubject = yearAndMonthSubjectInitial.getYear();
        monthSubject = yearAndMonthSubjectInitial.getMonthValue();

        tfYearSubject = new JFormattedTextField(getMaskFormatter());
        tfYearSubject.getDocument().addDocumentListener(new YearSubjectChangeListener());
        tfYearSubject.setPreferredSize(new Dimension(40, 20));
        panelYearMonthInputs.add(tfYearSubject);

        monthsNums = new String[]{"Jan [1]", "Feb [2]", "Mar [3]", "Apr [4]", "May [5]",
            "Jun [6]", "Jul [7]", "Aug [8]", "Sep [9]", "Oct [10]", "Nov [11]", "Dec [12]"};

        monthsList = new JComboBox<>(monthsNums);
        monthsList.addItemListener(new MonthSubjectChangeListener());
        monthsList.setSelectedIndex(yearAndMonthSubjectInitial.getMonthValue() - 1);
        panelYearMonthInputs.add(monthsList);

        panelMetaInputs.add(panelYearMonthInputs);

        panelDateOfTake = new JPanel();
        Border borderPaymentDate = BorderFactory.createTitledBorder("Payment date");
        panelDateOfTake.setBorder(borderPaymentDate);

        dateOfPayment = new DatePicker();
        dateOfPayment.setTodayAsDefault();
        boolDateFilled = true;
        DateListenerImpli dateListenerImpli = new DateListenerImpli();
        dateOfPayment.addDateListener(dateListenerImpli);
        dateOfPayment.addDateDeselectedListener(dateListenerImpli);
        panelDateOfTake.add(dateOfPayment.getDatePicker());
        panelMetaInputs.add(panelDateOfTake);

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
    }

    /**
     * Year of which the salary will take effect.
     *
     * @return String represent the year field input
     */
    public int getSubjectYear() {
        return Integer.parseInt(tfYearSubject.getText());
    }

    public LocalDate getDateSalaryGiven() {
        return dateOfPayment.getDate();
    }

    public boolean getBoolDateFilled() {
        return boolDateFilled;
    }

    public LocalDate getYearMonthSubjectOfSalary() {
        int year = getSubjectYear();
        Month month = Month.of(getSubjectMonth());

        LocalDate yearMonthSubjectOfSalary = LocalDate.of(year, month, 1);

        return yearMonthSubjectOfSalary;
    }

    public void setCompute(Compute compute) {
        this.compute = compute;
    }

    protected void clearInputFields() {
        tfYearSubject.setText(String.valueOf(yearAndMonthSubjectInitial.getYear()));
        monthsList.setSelectedIndex(yearAndMonthSubjectInitial.getMonthValue() - 1);
        dateOfPayment.setTodayAsDefault();
    }

    private void setFieldsEditable(boolean editable) {
        tfYearSubject.setEditable(editable);
        monthsList.setEnabled(editable);
        dateOfPayment.setEnabled(editable);
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

        tfYearSubject.setText(year.toString());
        monthsList.setSelectedIndex(month.getValue() - 1);
        dateOfPayment.setDateValue(salary.getDateGiven());

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

    @Override
    public void computed() {
        boolYearSubjectChanged = false;
        boolMonthSubjectChanged = false;
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

    private class YearSubjectChangeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            changed(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            changed(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            System.out.println("DocumentListener changedUpdate");
        }

        private void changed(DocumentEvent e) {
            String yearSubjectString = tfYearSubject.getText().trim();
            if (!yearSubjectString.isBlank()) {
                yearSubject = Integer.parseInt(yearSubjectString);
                if (compute.getYearSubjectOldValue() != yearSubject) {
                    yearAndMonthSubjectUpdatable = YearMonth.of(yearSubject, monthSubject);
                    boolYearSubjectChanged = true;
                    notifyYearOrMonthChanged(yearAndMonthSubjectUpdatable);
                } else {
                    boolYearSubjectChanged = false;
                    if (!boolMonthSubjectChanged) {
                        yearAndMonthSubjectUpdatable = YearMonth.of(yearSubject, monthSubject);
                        notifyYearAndMonthNotChanged(yearAndMonthSubjectUpdatable);
                    }
                }
            }
        }
    }

    class MonthSubjectChangeListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent event) {

            // Wait until compute initialized and referenced properly.
            if (compute == null) {
                return;
            }

            JComboBox source = (JComboBox) event.getSource();
            if (source == monthsList) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    monthSubject = source.getSelectedIndex() + 1;
                    if (compute.getMonthSubjectOldValue() != monthSubject) {
                        boolMonthSubjectChanged = true;
                        yearAndMonthSubjectUpdatable = YearMonth.of(yearSubject, monthSubject);
                        notifyYearOrMonthChanged(yearAndMonthSubjectUpdatable);
                    }
                }
            }
        }
    }

    public void addSubjectDateChangeListener(SubjectDateChangeListener sdchl) {
        this.subjectDateChangeListeners.add(sdchl);
    }

    private void notifyYearOrMonthChanged(YearMonth yearMonthSubject) {
        this.subjectDateChangeListeners.forEach((sdchl) -> {
            sdchl.yearOrMonthChanged(yearMonthSubject);
        });
    }

    private void notifyYearAndMonthNotChanged(YearMonth yearMonthSubject) {
        this.subjectDateChangeListeners.forEach((sdchl) -> {
            sdchl.yearAndMonthNotChanged(yearMonthSubject);
        });
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
            mask.setPlaceholder(String.valueOf(yearAndMonthSubjectInitial.getYear()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mask;
    }
}
