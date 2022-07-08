package gui.salaryadvance;

import crud.CreateListener;
import crud.DeleteListener;
import crud.UpdateICRPListener;
import crud.UpdateListener;
import datalink.CRUDSalaryAdvance;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import model.Employee;
import model.SalaryAdvance;

/**
 *
 * @author Saleh
 */
public class SalaryAdvanceInput
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
    private JFormattedTextField tfYearSubject;
    private JComboBox monthsList;
    private final String[] monthsNums;
    private DatePicker dateAdvanceGiven;
    private JLabel lbAmount;
    private JTextField tfAmount;
    private Color colorFieldRight;
    private Color colorFieldWrong;
    private Color colorDisabled;
    private boolean boolDateFilled;
    private boolean boolTfAmountFilled;
    private boolean boolSalaryAdvanceDisplayMode, boolEditMode, boolCreated, boolUpdated;
    private SalaryAdvance salaryAdvance;
    private int salaryAdvanceId;
    private int salaryAdvanceOldId;

    public SalaryAdvanceInput() {

        panelMetaInputs = new JPanel();

        GridBagConstraints c;

        colorFieldRight = new Color(226, 252, 237);
        colorFieldWrong = new Color(254, 225, 214);
        colorDisabled = new Color(105, 105, 105);

        DocumentRegex docRegx = new DocumentRegex();

        panelYearMonthInputs = new JPanel();
        Border borderSubjectDate = BorderFactory.createTitledBorder("Subject date");
        panelYearMonthInputs.setBorder(borderSubjectDate);

        LocalDate today = LocalDate.now();
        yearAndMonth = YearMonth.of(today.getYear(), today.getMonthValue());

        tfYearSubject = new JFormattedTextField(getMaskFormatter());
        tfYearSubject.setPreferredSize(new Dimension(40, 20));
        panelYearMonthInputs.add(tfYearSubject);

        monthsNums = new String[]{"Jan [1]", "Feb [2]", "Mar [3]", "Apr [4]", "May [5]",
            "Jun [6]", "Jul [7]", "Aug [8]", "Sep [9]", "Oct [10]", "Nov [11]", "Dec [12]"};

        monthsList = new JComboBox<>(monthsNums);
        monthsList.setSelectedIndex(yearAndMonth.getMonthValue() - 1);
        panelYearMonthInputs.add(monthsList);

        panelMetaInputs.add(panelYearMonthInputs);

        Border borderPaymentDate = BorderFactory.createTitledBorder("Payment date");
        panelDateOfTake = new JPanel();
        panelDateOfTake.setBorder(borderPaymentDate);

        dateAdvanceGiven = new DatePicker();
        dateAdvanceGiven.setTodayAsDefault();
        boolDateFilled = true;
        DateListenerImpli dateListenerImpli = new DateListenerImpli();
        dateAdvanceGiven.addDateListener(dateListenerImpli);
        dateAdvanceGiven.addDateDeselectedListener(dateListenerImpli);
        panelDateOfTake.add(dateAdvanceGiven.getDatePicker());
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

    public JPanel getSalaryAdvanceInputsPanel() {
        return mainPanel;
    }

    @Override
    public void employeeSelected(Employee employee) {
        if (!boolSalaryAdvanceDisplayMode) {
            setFieldsEditable(true);
        }
        if (boolSalaryAdvanceDisplayMode && employee != null) {
            clearInputFields();
        }
    }

    @Override
    public void employeeDeselected() {
        setFieldsEditable(false);
    }

    /**
     * Year of which the salary advance will take effect. That is the year that
     * advance will be deducted from.
     *
     * @return String represent the year field input
     */
    public String getSubjectYear() {
        return tfYearSubject.getText();
    }

    public LocalDate getDateAdvanceGiven() {
        return dateAdvanceGiven.getDate();
    }

    public BigDecimal getAmount() {
        return new BigDecimal(tfAmount.getText());
    }

    public boolean getBoolDateFilled() {
        return boolDateFilled;
    }

    public boolean getBoolTfAmountFilled() {
        return boolTfAmountFilled;
    }

    protected void clearInputFields() {
        tfYearSubject.setText(String.valueOf(yearAndMonth.getYear()));
        monthsList.setSelectedIndex(yearAndMonth.getMonthValue() - 1);
        dateAdvanceGiven.setTodayAsDefault();
        tfAmount.setText(null);
        boolTfAmountFilled = false;
    }

    private void setFieldsEditable(boolean editable) {
        tfYearSubject.setEditable(editable);
        monthsList.setEnabled(editable);
        dateAdvanceGiven.setEnabled(editable);
        tfAmount.setEditable(editable);
        tfAmount.setForeground(editable ? null : colorDisabled);
    }

    @Override
    public void created() {
        boolCreated = true;
        tfAmount.setText(null);
    }

    @Override
    public void updated() {
        boolUpdated = true;
        if (boolSalaryAdvanceDisplayMode) {
            setFieldsEditable(false);
            setInputFieldsWithSalaryAdvance(salaryAdvanceId);
        }
    }

    @Override
    public void updatedICRP() {
        boolUpdated = true;
        if (boolSalaryAdvanceDisplayMode) {
            setFieldsEditable(false);
        }
    }

    @Override
    public void displayable() {
        boolSalaryAdvanceDisplayMode = true;
        clearInputFields();
        setFieldsEditable(false);
    }

    @Override
    public void unDisplayable() {
        boolSalaryAdvanceDisplayMode = false;
        boolEditMode = false;
        clearInputFields();
        setFieldsEditable(true);
    }

    @Override
    public void rowSelectedWithRecordId(int id) {

        salaryAdvanceId = id;

        if (boolSalaryAdvanceDisplayMode) {
            setInputFieldsWithSalaryAdvance(id);
        }
    }

    public void setInputFieldsWithSalaryAdvance(int id) {

        if (salaryAdvance == null || salaryAdvanceOldId != id || boolUpdated) {
            // If salaryAdvance object is null, or
            // if new Id is not the same as previous stored Id (salaryAdvanceOldId), or
            // if database entity update submitted
            // then make a new database request.
            boolUpdated = false; // reset submission flag
            salaryAdvance = CRUDSalaryAdvance.getById(id);
        }
        // else: otherwise use previously requested salaryAdvance object
        //

        LocalDate yearMonthSubject = salaryAdvance.getYearMonthSubject();
        java.time.Year year = Year.from(yearMonthSubject);
        Month month = Month.from(yearMonthSubject);

        tfYearSubject.setText(year.toString());
        monthsList.setSelectedIndex(month.getValue() - 1);
        dateAdvanceGiven.setDateValue(salaryAdvance.getDateGiven());
        tfAmount.setText(String.valueOf(salaryAdvance.getAmount()));

        // Store id for future comparsions,
        // you find comparsion at the top of this method
        salaryAdvanceOldId = salaryAdvance.getId();
    }

    @Override
    public void editable() {
        boolEditMode = true;
        setFieldsEditable(true);
    }

    @Override
    public void cancelled() {
        if (boolSalaryAdvanceDisplayMode) {
            // Display mode, and posibly edit mode
            boolEditMode = false;
            setFieldsEditable(false);
            setInputFieldsWithSalaryAdvance(salaryAdvanceId);
        } else if (!boolSalaryAdvanceDisplayMode) {
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
     * Month of which the salary advance will take effect. That is the month
     * that advance will be deducted from.
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
