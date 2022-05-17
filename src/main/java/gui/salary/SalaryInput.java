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
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
        ComputeListener,
        PaymnetListener {

    private JPanel mainPanel, panelCards;
    private JPanel panelMetaInputs, panelYearMonthInputs, panelDateOfTake;
    private YearMonth yearAndMonth;
    private JFormattedTextField tfYearSubject;
    private JComboBox monthsList;
    private final String[] monthsNums;
    private DatePicker dateOfPayment;
    private JLabel lbPayable, lbAlreadyPaid, lbPaymentPending, lbPaymentNonDetermined;
    private JTextField tfPayable;
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
    private URL urlTickMark;
    private ImageIcon imageIconTickMark;
    private final String STR_PANEL_NON_DETERMINED = "non determined";
    private final String STR_PANEL_PENDING = "pending";
    private final String STR_PANEL_PAIED = "paied";

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
        yearAndMonth = YearMonth.of(today.getYear(), today.getMonthValue());

        tfYearSubject = new JFormattedTextField(getMaskFormatter());
        tfYearSubject.getDocument().addDocumentListener(new YearSubjectChangeListener());
        tfYearSubject.setPreferredSize(new Dimension(40, 20));
        panelYearMonthInputs.add(tfYearSubject);

        monthsNums = new String[]{"Jan [1]", "Feb [2]", "Mar [3]", "Apr [4]", "May [5]",
            "Jun [6]", "Jul [7]", "Aug [8]", "Sep [9]", "Oct [10]", "Nov [11]", "Dec [12]"};

        monthsList = new JComboBox<>(monthsNums);
        monthsList.addItemListener(new MonthSubjectChangeListener());
        monthsList.setSelectedIndex(yearAndMonth.getMonthValue() - 1);
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

        lbPayable = new JLabel("Payable:");
        panelMetaInputs.add(lbPayable);

        tfPayable = new JTextField();
        tfPayable.setEditable(false);
        tfPayable.setFont(new Font("SansSerif", Font.BOLD, 12));
        tfPayable.setPreferredSize(new Dimension(75, 27));
        panelMetaInputs.add(tfPayable);

        lbPaymentNonDetermined = new JLabel("Non determined yet");

        lbPaymentPending = new JLabel("... Pending              ");

        urlTickMark = getClass().getResource("/images/tick.png");
        imageIconTickMark = new ImageIcon(urlTickMark);
        lbAlreadyPaid = new JLabel("Alreay Paid     ", imageIconTickMark, 0);

        JPanel panelLabelNonDeterminedYet = new JPanel();
        panelLabelNonDeterminedYet.add(lbPaymentNonDetermined);
        JPanel panelLabelPaymentPending = new JPanel();
        panelLabelPaymentPending.add(lbPaymentPending);
        JPanel panelLabelPaied = new JPanel();
        panelLabelPaied.add(lbAlreadyPaid);

        panelCards = new JPanel(new CardLayout());
        // Add all panels; paid, pending and non determined
        panelCards.add(panelLabelNonDeterminedYet, STR_PANEL_NON_DETERMINED);
        panelCards.add(panelLabelPaymentPending, STR_PANEL_PENDING);
        panelCards.add(panelLabelPaied, STR_PANEL_PAIED);
        // Show the empty panel
        CardLayout cl = (CardLayout) (panelCards.getLayout());
        cl.show(panelCards, STR_PANEL_NON_DETERMINED);
        panelMetaInputs.add(panelCards);

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
        CardLayout cl = (CardLayout) (panelCards.getLayout());
        cl.show(panelCards, STR_PANEL_NON_DETERMINED);
    }

    @Override
    public void employeeDeselected() {
        setFieldsEditable(false);
        tfPayable.setText(null);

        CardLayout cl = (CardLayout) (panelCards.getLayout());
        cl.show(panelCards, STR_PANEL_NON_DETERMINED);
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

    public void setTfPayable(String payable) {
        tfPayable.setText(payable);
    }

    public BigDecimal getPayable() {
        return new BigDecimal(tfPayable.getText());
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
        tfYearSubject.setText(String.valueOf(yearAndMonth.getYear()));
        monthsList.setSelectedIndex(yearAndMonth.getMonthValue() - 1);
        dateOfPayment.setTodayAsDefault();
        tfPayable.setText(null);
    }

    private void setFieldsEditable(boolean editable) {
        tfYearSubject.setEditable(editable);
        monthsList.setEnabled(editable);
        dateOfPayment.setEnabled(editable);
        tfPayable.setForeground(editable ? null : colorDisabled);
    }

    @Override
    public void created() {
        boolCreated = true;
        tfPayable.setText(null);
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

    @Override
    public void computed() {
        boolYearSubjectChanged = false;
        boolMonthSubjectChanged = false;
    }

    @Override
    public void cleared() {
        CardLayout cl = (CardLayout) (panelCards.getLayout());
        cl.show(panelCards, STR_PANEL_PAIED);
    }

    @Override
    public void pending() {
        CardLayout cl = (CardLayout) (panelCards.getLayout());
        cl.show(panelCards, STR_PANEL_PENDING);
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
            String yearSubject = tfYearSubject.getText().trim();
            if (!yearSubject.equals("")) {
                if (compute.getYearSubjectOldValue() != Integer.parseInt(yearSubject)) {
                    boolYearSubjectChanged = true;
                    notifyYearOrMonthChanged();
                } else {
                    boolYearSubjectChanged = false;
                    if (!boolMonthSubjectChanged) {
                        notifyYearAndMonthNotChanged();
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

            JComboBox c = (JComboBox) event.getSource();
            int monthSubject = c.getSelectedIndex() + 1;
            if (compute.getMonthSubjectOldValue() != monthSubject) {
                boolMonthSubjectChanged = true;
                notifyYearOrMonthChanged();
            } else {
                boolMonthSubjectChanged = false;
                if (!boolYearSubjectChanged) {
                    notifyYearAndMonthNotChanged();
                }
            }
        }
    }

    public void addSubjectDateChangeListener(SubjectDateChangeListener sdchl) {
        this.subjectDateChangeListeners.add(sdchl);
    }

    private void notifyYearOrMonthChanged() {
        this.subjectDateChangeListeners.forEach((sdchl) -> {
            sdchl.yearOrMonthChanged();
        });
    }

    private void notifyYearAndMonthNotChanged() {
        this.subjectDateChangeListeners.forEach((sdchl) -> {
            sdchl.yearAndMonthNotChanged();
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
            mask.setPlaceholder(String.valueOf(yearAndMonth.getYear()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mask;
    }
}
