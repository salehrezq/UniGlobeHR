package gui.performance;

import datalink.CRUDPerformance;
import datalink.CRUDPerformanceType;
import gui.EmployeeSelectedListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import model.Employee;
import model.Performance;

/**
 *
 * @author Saleh
 */
public class PerformanceRequest
        implements
        EmployeeSelectedListener,
        PerformanceDisplayableListener {

    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollTable;
    private JPanel panelGather;
    private JPanel panelControlls;
    private JPanel panelTable;
    private JButton btnRequestData;
    GridBagConstraints c;
    private List records;
    private int employeeId;
    private YearMonth yearAndMonth;
    private JFormattedTextField tfYear;
    private JComboBox monthsList;
    private final String[] monthsNums;
    private List<RowSelectedListener> rowSelectedListeners;
    private List<RowDeselectedListener> rowDeselectedListeners;
    private boolean boolRowSelected;
    private Integer performanceId;

    public PerformanceRequest() {
        super();

        rowSelectedListeners = new ArrayList<>();
        rowDeselectedListeners = new ArrayList<>();

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

        model = new DefaultTableModel(new String[]{"DateTime", "State", "Type", "Amount", "Title", "Performance Id"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Disable cells editing.
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(1).setPreferredWidth(5);
        table.getColumnModel().getColumn(2).setPreferredWidth(5);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getSelectionModel().addListSelectionListener(new RowSelectionListener());

        // Hide Description Id column; because its purpose is
        // intended only to be used for click event of the row
        // to be passed to other areas of the program.
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(5));
        scrollTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panelGather = new JPanel(new GridBagLayout());
        panelGather.setBorder(BorderFactory.createLineBorder(Color.red));
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        panelGather.add(panelControlls, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        panelGather.add(scrollTable, c);
    }

    public JPanel getPanelTable() {
        return this.panelGather;
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

    public List<Performance> getTata() {

        if (employeeId < 1) {
            return null;
        }

        int year = Integer.valueOf(tfYear.getText());
        int month = this.monthsList.getSelectedIndex() + 1;
        YearMonth ym = YearMonth.of(year, month);
        setYearAndMonth(ym);

        records = CRUDPerformance.getPerformanceRecordByEmployeeByMonth(employeeId, ym);

        return records;
    }

    @Override
    public void employeeSelected(Employee employee) {
        // cleare the model when new employee node selected.
        model.setRowCount(0);
        employeeId = employee.getId();
    }

    @Override
    public void employeeDeselected() {
        // cleare the model when no employee node selected.
        model.setRowCount(0);
        employeeId = -1;
    }

    @Override
    public void performanceDisplayable() {
        table.getSelectionModel().setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

        if (boolRowSelected && performanceId != null) {
            // React to selected row once display mode set to on:
            // In case if display mode button clicked
            // and at the same time some row was already selected,
            // then notify that a row is already selected
            // to get benefit from it or react accordingly.
            notifyRowSelectedListener(performanceId);
        }
    }

    @Override
    public void performanceUnDisplayable() {
        table.getSelectionModel().setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    private class ActionGetData implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            // Clear the model every time, to append fresh results
            // and not accumulate on previous results
            model.setRowCount(0);

            List<Performance> performanceList = getTata();

            if (performanceList == null) {
                JOptionPane.showConfirmDialog(panelGather,
                        "Select Employee", "",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }

            Object[] modelRow = new Object[6];

            int size = performanceList.size();
            for (int i = 0; i < size; i++) {
                Performance performance = performanceList.get(i);
                modelRow[0] = performance.getDateTime();
                modelRow[1] = performanceState(performance.getState());
                modelRow[2] = getTypeText(performance.getTypeId());
                modelRow[3] = performance.getAmount();
                modelRow[4] = performance.getTitle();
                modelRow[5] = performance.getId();
                model.addRow(modelRow);
            }
        }

        private String getTypeText(int typeID) {
            return CRUDPerformanceType.getById(typeID).getType();
        }

        private String performanceState(boolean state) {

            String stateType = "Undefined";

            if (state) {
                stateType = "Positive";
            } else {
                stateType = "Negative";
            }
            return stateType;
        }
    }

    public void addRowSelectedListener(RowSelectedListener rcl) {
        this.rowSelectedListeners.add(rcl);
    }

    private void notifyRowSelectedListener(int performanceId) {
        this.rowSelectedListeners.forEach((rcl) -> {
            rcl.rowSelectedWithRecordId(performanceId);
        });
    }

    public void addRowDeselectedListenerListener(RowDeselectedListener rowDeselectedListener) {
        this.rowDeselectedListeners.add(rowDeselectedListener);
    }

    private void notifyRowDeselection() {
        this.rowDeselectedListeners.forEach((rowDeselectedListener) -> {
            rowDeselectedListener.rowDeselection();
        });
    }

    private class RowSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent event) {

            if (!event.getValueIsAdjusting()) {
                // Ensure single event invoke
                DefaultListSelectionModel selectionModel = (DefaultListSelectionModel) event.getSource();

                if (selectionModel.isSelectionEmpty()) {
                    // Table row deselection occurred
                    boolRowSelected = false;
                    notifyRowDeselection();
                } else {
                    boolRowSelected = true;
                    int viewRow = table.getSelectedRow();
                    if (viewRow > -1) {
                        int performanceIdColumn = 5;
                        int modelRow = table.convertRowIndexToModel(viewRow);

                        Object performanceIdObject = table.getModel().getValueAt(modelRow, performanceIdColumn);
                        performanceId = Integer.parseInt(performanceIdObject.toString());
                        notifyRowSelectedListener(performanceId);
                    }
                }
            }

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
