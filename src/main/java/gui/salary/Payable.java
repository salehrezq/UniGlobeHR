package gui.salary;

import crud.CreateListener;
import crud.DeleteListener;
import crud.UpdateICRPListener;
import crud.UpdateListener;
import datalink.CRUDSalary;
import gui.EmployeeSelectedListener;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Employee;
import model.Salary;

/**
 *
 * @author Saleh
 */
public class Payable
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
        PaymnetListener,
        SubjectDateChangeListener,
        ComputeListener {

    private JPanel container, panelCards;
    private YearMonth yearAndMonth;
    private JLabel lbPayable,
            lbJustPaid,
            lbAlreadyPaid,
            lbPaymentPending,
            lbNotPaidYet,
            lbNoEmployeeSelected;
    private JTextField tfPayable;
    private Color colorFieldRight;
    private Color colorFieldWrong;
    private Color colorDisabled;
    private boolean boolSalaryDisplayMode, boolEditMode,
            boolCreated, boolUpdated, boolMonthSubjectChanged, boolSalaryPaid;
    private Salary salary, salaryTemp;
    private int salaryId;
    private int salaryOldId;
    private Compute compute;
    private URL urlTickMark;
    private ImageIcon imageIconTickMark;
    private SalaryInput salaryInput;
    private Employee employee;

    public Payable() {

        container = new JPanel();

        GridBagConstraints c;

        colorFieldRight = new Color(226, 252, 237);
        colorFieldWrong = new Color(254, 225, 214);
        colorDisabled = new Color(105, 105, 105);

        lbPayable = new JLabel("Payable:");
        container.add(lbPayable);

        tfPayable = new JTextField();
        tfPayable.setEditable(false);
        tfPayable.setFont(new Font("SansSerif", Font.BOLD, 12));
        tfPayable.setPreferredSize(new Dimension(75, 27));
        container.add(tfPayable);

        lbNoEmployeeSelected = new JLabel("No Employee selected       ");
        lbNotPaidYet = new JLabel("Not Paid Yet                     ");
        lbPaymentPending = new JLabel("... Pending                         ");

        urlTickMark = getClass().getResource("/images/tick.png");
        imageIconTickMark = new ImageIcon(urlTickMark);
        lbJustPaid = new JLabel("Successfully Paid        ", imageIconTickMark, 0);
        lbAlreadyPaid = new JLabel("Already Paid               ", imageIconTickMark, 0);

        JPanel panelLabelNoEmployeeSelected = new JPanel();
        panelLabelNoEmployeeSelected.add(lbNoEmployeeSelected);
        JPanel panelLabelNotPaidYet = new JPanel();
        panelLabelNotPaidYet.add(lbNotPaidYet);
        JPanel panelLabelPaymentPending = new JPanel();
        panelLabelPaymentPending.add(lbPaymentPending);
        JPanel panelLabelJustPaid = new JPanel();
        panelLabelJustPaid.add(lbJustPaid);
        JPanel panelLabelAlreadyPaid = new JPanel();
        panelLabelAlreadyPaid.add(lbAlreadyPaid);

        panelCards = new JPanel(new CardLayout());
        container.add(panelCards);
        // Add all panels:
        panelCards.add(panelLabelNoEmployeeSelected, PaymentState.NO_EMPLOYEE_SELECTED.state());
        panelCards.add(panelLabelNotPaidYet, PaymentState.NOT_PAID.state());
        panelCards.add(panelLabelPaymentPending, PaymentState.PENDING.state());
        panelCards.add(panelLabelJustPaid, PaymentState.JUST_PAID.state());
        panelCards.add(panelLabelAlreadyPaid, PaymentState.ALREADY_PAID.state());
        // Show the no employee selected
        setPaymentPanelCardState(PaymentState.NO_EMPLOYEE_SELECTED.state());

        setFieldsEditable(false);
    }

    @Override
    public void yearOrMonthChanged(YearMonth yearMonth) {
        if (employee != null) {
            LocalDate yearMonthSubjectOfSalary = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
            salaryTemp = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), yearMonthSubjectOfSalary);
            boolSalaryPaid = salaryTemp != null;
            if (boolSalaryPaid) {
                tfPayable.setText(salaryTemp.getPayable().toPlainString());
                setPaymentPanelCardState(PaymentState.ALREADY_PAID.state());
            } else {
                tfPayable.setText(null);
                setPaymentPanelCardState(PaymentState.NOT_PAID.state());
            }
        }
    }

    @Override
    public void yearAndMonthNotChanged(YearMonth yearMonth) {
        if (employee != null) {
            LocalDate yearMonthSubjectOfSalary = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
            salaryTemp = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), yearMonthSubjectOfSalary);
            boolSalaryPaid = salaryTemp != null;
            if (boolSalaryPaid) {
                tfPayable.setText(salaryTemp.getPayable().toPlainString());
                setPaymentPanelCardState(PaymentState.ALREADY_PAID.state());
            } else {
                tfPayable.setText(null);
                setPaymentPanelCardState(PaymentState.NOT_PAID.state());
            }
        }
    }

    @Override
    public void computed(BigDecimal amount) {
        tfPayable.setText(amount.toPlainString());
    }

    private enum PaymentState {

        NO_EMPLOYEE_SELECTED("no employee selected"),
        NOT_PAID("was not paid yet"),
        PENDING("pending"),
        JUST_PAID("just paid"),
        ALREADY_PAID("paid");

        private final String state;

        private PaymentState(String state) {
            this.state = state;
        }

        private String state() {
            return state;
        }
    }

    private void setPaymentPanelCardState(String state) {
        CardLayout cl = (CardLayout) (panelCards.getLayout());
        cl.show(panelCards, state);
    }

    public JPanel getContainer() {
        return container;
    }

    public void setSalaryInput(SalaryInput salaryInput) {
        this.salaryInput = salaryInput;
    }

    @Override
    public void employeeSelected(Employee employee) {
        this.employee = employee;
        salaryTemp = CRUDSalary.isEmployeeWithYearMonthSubjectExist(employee.getId(), salaryInput.getYearMonthSubjectOfSalary());
        boolSalaryPaid = salaryTemp != null;
        if (boolSalaryPaid) {
            tfPayable.setText(salaryTemp.getPayable().toPlainString());
            setPaymentPanelCardState(PaymentState.ALREADY_PAID.state());
        } else {
            tfPayable.setText(null);
            if (!boolSalaryDisplayMode) {
                setFieldsEditable(true);
            }
            if (boolSalaryDisplayMode && employee != null) {
                clearInputFields();
            }
            setPaymentPanelCardState(PaymentState.NOT_PAID.state());
        }
    }

    @Override
    public void employeeDeselected() {
        employee = null;
        setFieldsEditable(false);
        tfPayable.setText(null);
        setPaymentPanelCardState(PaymentState.NO_EMPLOYEE_SELECTED.state());
    }

    public void setTfPayable(String payable) {
        tfPayable.setText(payable);
    }

    public BigDecimal getPayable() {
        return new BigDecimal(tfPayable.getText());
    }

    public void setCompute(Compute compute) {
        this.compute = compute;
    }

    protected void clearInputFields() {
        tfPayable.setText(null);
    }

    private void setFieldsEditable(boolean editable) {
        // Comment out for the moment, later we may need it.
        // tfPayable.setForeground(editable ? null : colorDisabled);
    }

    @Override
    public void created() {
        boolCreated = true;
        setPaymentPanelCardState(PaymentState.JUST_PAID.state());
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
        setPaymentPanelCardState(PaymentState.NOT_PAID.state());
    }

    @Override
    public void cleared() {
        setPaymentPanelCardState(PaymentState.ALREADY_PAID.state());
    }

    @Override
    public void pending() {
        setPaymentPanelCardState(PaymentState.PENDING.state());
    }
}
