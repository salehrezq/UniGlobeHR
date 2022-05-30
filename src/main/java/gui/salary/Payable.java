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
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.net.URL;
import java.time.YearMonth;
import java.util.ArrayList;
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
        PaymnetListener {

    private JPanel container, panelFields, panelCards;
    private YearMonth yearAndMonth;
    private JLabel lbPayable, lbAlreadyPaid, lbPaymentPending, lbPaymentNonDetermined;
    private JTextField tfPayable;
    private Color colorFieldRight;
    private Color colorFieldWrong;
    private Color colorDisabled;
    private boolean boolSalaryDisplayMode, boolEditMode,
            boolCreated, boolUpdated, boolMonthSubjectChanged;
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
    private SalaryInput salaryInput;

    public Payable() {

        subjectDateChangeListeners = new ArrayList<>();
        panelFields = new JPanel();

        GridBagConstraints c;

        colorFieldRight = new Color(226, 252, 237);
        colorFieldWrong = new Color(254, 225, 214);
        colorDisabled = new Color(105, 105, 105);

        lbPayable = new JLabel("Payable:");
        panelFields.add(lbPayable);

        tfPayable = new JTextField();
        tfPayable.setEditable(false);
        tfPayable.setFont(new Font("SansSerif", Font.BOLD, 12));
        tfPayable.setPreferredSize(new Dimension(75, 27));
        panelFields.add(tfPayable);

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
        panelFields.add(panelCards);

        container = new JPanel(new GridBagLayout());

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 0, 5, 30);
        c.weightx = 1.0;
        c.weighty = 1.0;
        container.add(panelFields, c);

        setFieldsEditable(false);
    }

    public JPanel getContainer() {
        return container;
    }

    public void setSalaryInput(SalaryInput salaryInput) {
        this.salaryInput = salaryInput;
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

    public void addSubjectDateChangeListener(SubjectDateChangeListener sdchl) {
        this.subjectDateChangeListeners.add(sdchl);
    }
}
