/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendance;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import model.Employee;
import gui.EmployeeSelectedListener;
import datalink.CRUDAttendance;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import model.Attendance;

/**
 *
 * @author Saleh
 */
public class MonthelyAbsence implements EmployeeSelectedListener {

    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollTable;
    private JPanel panelGather;
    private JPanel panelControlls;
    private JPanel panelTable;
    private JButton btnRequestData;
    private List records;
    private int employeeId;
    private YearMonth yearAndMonth;
    private JFormattedTextField tfYear;
    private JComboBox monthsList;
    private final String[] monthsNums;

    public MonthelyAbsence() {
        super();

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

        model = new DefaultTableModel(new String[]{"Day Name", "Day Num"}, 0);
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        scrollTable = new JScrollPane(table);
        panelTable = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        c = new GridBagConstraints();
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        panelTable.add(scrollTable, c);

        panelGather = new JPanel(new GridBagLayout());
        panelGather.setBorder(BorderFactory.createLineBorder(Color.red));
        c = new GridBagConstraints();
        c.gridy = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.PAGE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        panelGather.add(panelControlls, c);

        c = new GridBagConstraints();
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        panelGather.add(panelTable, c);
    }

    public JPanel getPanelTable() {
        return this.panelGather;
    }

    public void setSelectedEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setYearAndMonth(YearMonth ym) {
        this.yearAndMonth = ym;
    }

    public List getTata() {

        if (employeeId < 1) {
            return null;
        }

        int year = Integer.valueOf(tfYear.getText());
        int month = this.monthsList.getSelectedIndex() + 1;
        YearMonth ym = YearMonth.of(year, month);

        records = CRUDAttendance.getAbsenceRecordByEmployeeByMonth(employeeId, ym);
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

    private class ActionGetData implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            // Clear the model every time, to append fresh results
            // and not accumulate on previous results
            model.setRowCount(0);

            List<Attendance> absentsList = getTata();

            if (absentsList == null) {
                JOptionPane.showConfirmDialog(panelGather,
                        "Select Employee", "",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                return;
            }

            absentsList.forEach((item) -> {
                Attendance absentRecord = (Attendance) item;
                model.addRow(this.getModelData(absentRecord));
            });
        }

        private Object[] getModelData(Attendance absentRecord) {
            Object[] objModel = new Object[2];
            LocalDate date = absentRecord.getDate();
            objModel[0] = date.getDayOfWeek().toString();
            objModel[1] = date.getDayOfMonth();
            return objModel;
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
