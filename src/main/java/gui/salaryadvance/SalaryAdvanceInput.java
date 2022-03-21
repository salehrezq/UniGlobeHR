package gui.salaryadvance;

import crud.CreateListener;
import crud.DeleteListener;
import crud.UpdateListener;
import datalink.CRUDPerformance;
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
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import model.Employee;
import model.Performance;
import model.PerformanceType;

/**
 *
 * @author Saleh
 */
public class SalaryAdvanceInput
        implements
        EmployeeSelectedListener,
        CreateListener,
        UpdateListener,
        DisplayableListener,
        RowSelectedListener,
        EditableListener,
        CancelListener,
        DeleteListener {

    private JPanel mainPanel;
    private JPanel panelMetaInputs, panelYearMonthInputs, panelDateOfTake;
    private YearMonth yearAndMonth;
    private JFormattedTextField tfYear;
    private JComboBox monthsList;
    private final String[] monthsNums;
    private DatePicker dateAdvanceTaken;
    private PerformanceType selectedPerformanceType;
    private JLabel lbAmount;
    private JTextField tfAmount;
    private Color colorFieldRight;
    private Color colorFieldWrong;
    private Color colorDisabled;
    private boolean boolTfTimeFilled;
    private boolean boolDateFilled;
    private boolean boolComboStateFilled;
    private boolean boolComboTypeFilled;
    private boolean boolTfAmountFilled;
    private boolean boolPerformanceDisplayMode, boolEditMode, boolCreated, boolUpdated;
    private Performance performance;
    private int performanceId;
    private int performanceOldId;

    public SalaryAdvanceInput() {

        panelMetaInputs = new JPanel();

        GridBagConstraints c;

        colorFieldRight = new Color(226, 252, 237);
        colorFieldWrong = new Color(254, 225, 214);
        colorDisabled = new Color(105, 105, 105);

        DocumentRegex docRegx = new DocumentRegex();

        Border borderYearMonthOfAdvance = BorderFactory.createTitledBorder("Year/Month of advance");

        panelYearMonthInputs = new JPanel();
        panelYearMonthInputs.setBorder(borderYearMonthOfAdvance);

        LocalDate today = LocalDate.now();
        yearAndMonth = YearMonth.of(today.getYear(), today.getMonthValue());

        tfYear = new JFormattedTextField(getMaskFormatter());
        tfYear.setPreferredSize(new Dimension(40, 20));
        panelYearMonthInputs.add(tfYear);

        monthsNums = new String[]{"Jan [1]", "Feb [2]", "Mar [3]", "Apr [4]", "May [5]",
            "Jun [6]", "Jul [7]", "Aug [8]", "Sep [9]", "Oct [10]", "Nov [11]", "Dec [12]"};

        monthsList = new JComboBox<>(monthsNums);
        monthsList.setSelectedIndex(yearAndMonth.getMonthValue() - 1);
        panelYearMonthInputs.add(monthsList);

        panelMetaInputs.add(panelYearMonthInputs);

        Border borderDateOfTake = BorderFactory.createTitledBorder("Date of take");
        panelDateOfTake = new JPanel();
        panelDateOfTake.setBorder(borderDateOfTake);

        dateAdvanceTaken = new DatePicker();
        dateAdvanceTaken.setTodayAsDefault();
        boolDateFilled = true;
        DateListenerImpli dateListenerImpli = new DateListenerImpli();
        dateAdvanceTaken.addDateListener(dateListenerImpli);
        dateAdvanceTaken.addDateDeselectedListener(dateListenerImpli);
        panelDateOfTake.add(dateAdvanceTaken.getDatePicker());
        panelMetaInputs.add(panelDateOfTake);

        ItemChangeListener comboBoxListener = new ItemChangeListener();

        lbAmount = new JLabel("Amount:");
        panelMetaInputs.add(lbAmount);

        tfAmount = new JTextField();
        tfAmount.setFont(new Font("SansSerif", Font.BOLD, 12));
        tfAmount.setBackground(colorFieldRight);
        tfAmount.getDocument().addDocumentListener(docRegx);
        tfAmount.setPreferredSize(new Dimension(95, 27));
        panelMetaInputs.add(tfAmount);

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

    public JPanel getPerformanceInputsPanel() {
        return mainPanel;
    }

    @Override
    public void employeeSelected(Employee employee) {
        if (!boolPerformanceDisplayMode) {
            setFieldsEditable(true);
        }
        if (boolPerformanceDisplayMode && employee != null) {
            clearInputFields();
        }
    }

    @Override
    public void employeeDeselected() {
        setFieldsEditable(false);
    }

    public String getYear() {
        return tfYear.getText();
    }

    public LocalDate getDateAdvanceTaken() {
        return dateAdvanceTaken.getDate();
    }

    public PerformanceType getPerformanceType() {
        return this.selectedPerformanceType;
    }

    public BigDecimal getAmount() {
        return new BigDecimal(tfAmount.getText());
    }

    public boolean getBoolTfTimeFilled() {
        return boolTfTimeFilled;
    }

    public boolean getBoolDateFilled() {
        return boolDateFilled;
    }

    public boolean getBoolComboState() {
        return boolComboStateFilled;
    }

    public boolean getBoolComboType() {
        return boolComboTypeFilled;
    }

    public boolean getBoolTfAmountFilled() {
        return boolTfAmountFilled;
    }

    protected void clearInputFields() {
        tfYear.setText(String.valueOf(yearAndMonth.getYear()));
        boolTfTimeFilled = false;
        dateAdvanceTaken.setTodayAsDefault();
        // Setting comboStateOfPerformance selected index to zero
        // invokes ItemListener methods which contains the code
        // to set the other linked combo box to zero
        // and also the boolean values to false.
        tfAmount.setText(null);
        boolTfAmountFilled = false;
    }

    private void setFieldsEditable(boolean editable) {

        tfYear.setEditable(editable);
        tfYear.setForeground(editable ? null : colorDisabled);
        monthsList.setEnabled(editable);
        dateAdvanceTaken.setEnabled(editable);
        tfAmount.setEditable(editable);
        tfAmount.setForeground(editable ? null : colorDisabled);
    }

    @Override
    public void created() {
        boolCreated = true;
        clearInputFields();
    }

    @Override
    public void updated() {
        boolUpdated = true;
        if (boolPerformanceDisplayMode) {
            setFieldsEditable(false);
            setInputFieldsWithPerformance(performanceId);
        }
    }

    @Override
    public void displayable() {
        boolPerformanceDisplayMode = true;
        clearInputFields();
        setFieldsEditable(false);
    }

    @Override
    public void unDisplayable() {
        boolPerformanceDisplayMode = false;
        boolEditMode = false;
        clearInputFields();
        setFieldsEditable(true);
    }

    @Override
    public void rowSelectedWithRecordId(int id) {

        performanceId = id;

        if (boolPerformanceDisplayMode) {
            setInputFieldsWithPerformance(id);
        }
    }

    public void setInputFieldsWithPerformance(int id) {

        if (performance == null || performanceOldId != id || boolUpdated) {
            // If performance object is null, or
            // if new Id is not the same as previous stored Id (performanceOldId), or
            // if database entity update submitted
            // then make a new database request.
            boolUpdated = false; // reset submission flag
            performance = CRUDPerformance.getById(id);
        }
        // else: otherwise use previously requested performance object
        //
        LocalDateTime ldt = performance.getDateTime();

        String localTime12 = ldt.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));

        tfYear.setText(localTime12);
        dateAdvanceTaken.setDateValue(ldt.toLocalDate());

        tfAmount.setText(String.valueOf(performance.getAmount()));
        String description = performance.getDescription();

        // Store id for future comparsions,
        // you find comparsion at the top of this method
        performanceOldId = performance.getId();
    }

    @Override
    public void editable() {
        boolEditMode = true;
        setFieldsEditable(true);
    }

    @Override
    public void cancelled() {
        if (boolPerformanceDisplayMode) {
            // Display mode, and posibly edit mode
            boolEditMode = false;
            setFieldsEditable(false);
            setInputFieldsWithPerformance(performanceId);
        } else if (!boolPerformanceDisplayMode) {
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

    class DocumentRegex implements DocumentListener {

        private void doWork() {

//            if (tfTime.getText().isEmpty()) {
//                tfTime.setBackground(colorFieldRight);
//            } else {
//                if (tfTime.getText().matches("^(0[1-9]|1[0-2]):([0-5][0-9]) ((a|p)m|(A|P)M)$")) {
//                    tfTime.setBackground(colorFieldRight);
//                    boolTfTimeFilled = true;
//                } else {
//                    tfTime.setBackground(colorFieldWrong);
//                    boolTfTimeFilled = false;
//                }
//            }
            if (tfAmount.getText().isEmpty()) {
                tfAmount.setBackground(colorFieldRight);
            } else {
                // Regex match DECIMAL(12,3)
                if (tfAmount.getText().matches("^\\d{0,9}(?:(?<=\\d)\\.(?=\\d)\\d{0,3})?$")) {
                    tfAmount.setBackground(colorFieldRight);
                    boolTfAmountFilled = true;
                } else {
                    tfAmount.setBackground(colorFieldWrong);
                    boolTfAmountFilled = false;
                }
            }
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            doWork();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            doWork();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            doWork();
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
