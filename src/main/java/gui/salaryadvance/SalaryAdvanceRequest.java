package gui.salaryadvance;

import crud.ReadListener;
import crud.UpdateICRPListener;
import crud.UpdateListener;
import datalink.CRUDSalaryAdvance;
import gui.EmployeeSelectedListener;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;
import model.Employee;
import model.SalaryAdvance;

/**
 *
 * @author Saleh
 */
public class SalaryAdvanceRequest
        implements
        EmployeeSelectedListener,
        DisplayableListener,
        EditableListener,
        CancelListener,
        UpdateListener,
        UpdateICRPListener {

    private JPanel panelControlls;
    private JButton btnRequestData;
    GridBagConstraints c;
    private List records;
    private int employeeId;
    private YearMonth yearAndMonth;
    private JFormattedTextField tfYear;
    private JComboBox monthsList;
    private final String[] monthsNums;
    private boolean boolDisplayMode;
    private List<ReadListener> readListeners;

    public SalaryAdvanceRequest() {
        super();

        readListeners = new ArrayList<>();

        LocalDate today = LocalDate.now();
        yearAndMonth = YearMonth.of(today.getYear(), today.getMonthValue());

        panelControlls = new JPanel();

        btnRequestData = new JButton("Request");
        btnRequestData.addActionListener(new ActionGetData());

        monthsNums = new String[]{"Jan [1]", "Feb [2]", "Mar [3]", "Apr [4]", "May [5]",
            "Jun [6]", "Jul [7]", "Aug [8]", "Sep [9]", "Oct [10]", "Nov [11]", "Dec [12]"};

        tfYear = new JFormattedTextField(getMaskFormatter());
        tfYear.setPreferredSize(new Dimension(40, 20));

        monthsList = new JComboBox<>(monthsNums);
        monthsList.setSelectedIndex(yearAndMonth.getMonthValue() - 1);

        panelControlls.add(btnRequestData);
        panelControlls.add(tfYear);
        panelControlls.add(monthsList);
    }

    public JPanel getPanelControls() {
        return this.panelControlls;
    }

    public void setSelectedEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setYearAndMonth(YearMonth ym) {
        this.yearAndMonth = ym;
    }

    public List<SalaryAdvance> getTata() {

        if (employeeId < 1) {
            return null;
        }

        int year = Integer.valueOf(tfYear.getText());
        int month = this.monthsList.getSelectedIndex() + 1;
        YearMonth ym = YearMonth.of(year, month);
        setYearAndMonth(ym);

        records = CRUDSalaryAdvance.getSalaryAdvancesRecordByEmployeeByMonth(employeeId, ym);

        return records;
    }

    @Override
    public void employeeSelected(Employee employee) {
        employeeId = employee.getId();
    }

    @Override
    public void employeeDeselected() {
        employeeId = -1;
    }

    @Override
    public void displayable() {
        boolDisplayMode = true;
    }

    @Override
    public void unDisplayable() {
        boolDisplayMode = false;
    }

    @Override
    public void editable() {
        btnRequestData.setEnabled(false);
        tfYear.setEnabled(false);
        monthsList.setEnabled(false);
    }

    private void restoreRowSelection() {
        if (boolDisplayMode) {
            btnRequestData.setEnabled(true);
            tfYear.setEnabled(true);
            monthsList.setEnabled(true);
        }
    }

    @Override
    public void cancelled() {
        restoreRowSelection();
    }

    @Override
    public void updated() {
        restoreRowSelection();
        btnRequestData.setEnabled(true);
        tfYear.setEnabled(true);
        monthsList.setEnabled(true);
    }

    @Override
    public void updatedICRP() {
        btnRequestData.setEnabled(true);
        tfYear.setEnabled(true);
        monthsList.setEnabled(true);
    }

    public void addReadListener(ReadListener readListener) {
        this.readListeners.add(readListener);
    }

    private void notifyRead(List<SalaryAdvance> records) {
        this.readListeners.forEach((readListener) -> {
            readListener.read(records);
        });
    }

    private class ActionGetData implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            notifyRead(getTata());
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
