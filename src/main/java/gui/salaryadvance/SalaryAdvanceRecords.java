package gui.salaryadvance;

import crud.DeleteListener;
import crud.ReadListener;
import crud.UpdateListener;
import datalink.CRUDSalaryAdvance;
import gui.EmployeeSelectedListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import model.Employee;
import model.SalaryAdvance;

/**
 *
 * @author Saleh
 */
public class SalaryAdvanceRecords
        implements
        EmployeeSelectedListener,
        DisplayableListener,
        EditableListener,
        CancelListener,
        UpdateListener,
        ReadListener<SalaryAdvance> {

    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollTable;
    private JPanel panelTable;
    GridBagConstraints c;
    private List<RowSelectedListener> rowSelectedListeners;
    private List<RowDeselectedListener> rowDeselectedListeners;
    private List<DeleteListener> deleteListeners;
    private boolean boolRowSelected;
    private Integer salaryAdvanceId;
    private boolean boolEditMode;
    private boolean boolDisplayMode;
    private final String selectedRowKey = "selectedRow";
    private Integer selectedModelRow;
    private Integer oldSelectedModelRow;
    private final Color rowSelectionColor;
    private final JPopupMenu popupMenu;
    private final JMenuItem menuItemDelete;
    private SalaryAdvance salaryAdvanceBeforeUpdate;

    public SalaryAdvanceRecords() {
        super();

        rowSelectedListeners = new ArrayList<>();
        rowDeselectedListeners = new ArrayList<>();
        deleteListeners = new ArrayList<>();

        panelTable = new JPanel();

        model = new DefaultTableModel(new String[]{"Date taken", "Amount", "SalaryAdvance Id"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Disable cells editing.
                return false;
            }
        };

        popupMenu = new JPopupMenu();
        popupMenu.addPopupMenuListener(new SelectRowOnMouseRightClick());
        menuItemDelete = new JMenuItem("Delete record");
        menuItemDelete.addActionListener(new MenuItemAction());
        popupMenu.add(menuItemDelete);

        table = new JTable(model);

        rowSelectionColor = new Color(184, 207, 229);
        TableColumnModel columnModel = table.getColumnModel();
        int columnsCount = columnModel.getColumnCount();

        LockableTableRowCellRenderer cellRenderer = new LockableTableRowCellRenderer();

        for (int column = 0; column < columnsCount; column++) {
            columnModel.getColumn(column).setCellRenderer(cellRenderer);
        }

        table.setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setPreferredWidth(1);
        table.getColumnModel().getColumn(1).setPreferredWidth(1);
        table.getColumnModel().getColumn(2).setPreferredWidth(1);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getSelectionModel().addListSelectionListener(new RowSelectionListener());

        // Hide Description Id column; because its purpose is
        // intended only to be used for click event of the row
        // to be passed to other areas of the program.
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(2));
        scrollTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public JScrollPane getPanelTable() {
        return this.scrollTable;
    }

    @Override
    public void employeeSelected(Employee employee) {
        // cleare the model when new employee node selected.
        model.setRowCount(0);
    }

    @Override
    public void employeeDeselected() {
        // cleare the model when no employee node selected.
        model.setRowCount(0);
    }

    @Override
    public void displayable() {

        if (boolRowSelected) {
            table.setComponentPopupMenu(popupMenu);
        }
        boolDisplayMode = true;

        table.getSelectionModel().setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

        if (boolRowSelected && salaryAdvanceId != null) {
            // React to selected row once display mode set to on:
            // In case if display mode button clicked
            // and at the same time some row was already selected,
            // then notify that a row is already selected
            // to get benefit from it or react accordingly.
            notifyRowSelectedListener(salaryAdvanceId);
        }
    }

    @Override
    public void unDisplayable() {
        table.setComponentPopupMenu(null);
        boolDisplayMode = false;
        table.getSelectionModel().setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    @Override
    public void editable() {
        // Save copy of current record before update
        // for comparsion with the new updated record
        salaryAdvanceBeforeUpdate = CRUDSalaryAdvance.getById(salaryAdvanceId);
        // Edit mode enabled
        boolEditMode = true;
        // Save selected row for future use
        oldSelectedModelRow = selectedModelRow;
        // Disable row selection
        table.setRowSelectionAllowed(false);
        // Update table with selected row to be used to
        // render the table with selected row locked
        table.putClientProperty(selectedRowKey, selectedModelRow);
        // Remove popupMenu
        table.setComponentPopupMenu(null);
    }

    private void restoreRowSelection() {
        if (boolDisplayMode) {
            // Edit mode cancelled
            boolEditMode = false;
            // Enable row selection
            table.setRowSelectionAllowed(true);
            // Update table to remove selection color
            table.putClientProperty(selectedRowKey, null);
            // Keep same selected row as selected, but not locked
            table.setRowSelectionInterval(0, oldSelectedModelRow);
            // Enable/set popupMenu
            if (boolRowSelected) {
                table.setComponentPopupMenu(popupMenu);
            }
        }
    }

    @Override
    public void cancelled() {
        restoreRowSelection();
    }

    @Override
    public void updated() {
        restoreRowSelection();

        SalaryAdvance salaryAdvance = CRUDSalaryAdvance.getById(salaryAdvanceId);

        // Compare the advance subject date between old and updated record
        if (!(salaryAdvance.getYearMonthSubject().isEqual(salaryAdvanceBeforeUpdate.getYearMonthSubject()))) {
            // If subject_year_month has been changed (different value) then the
            // record should not appear against the same requested subject_year_month
            // attributes, because they are no longer relevant after the change
            model.removeRow(selectedModelRow);
        } else {
            // If other attributes changed: amount, date_taken; it is logical to keep it.
            table.getModel().setValueAt(salaryAdvance.getDateTaken(), oldSelectedModelRow, 0);
            table.getModel().setValueAt(salaryAdvance.getAmount(), oldSelectedModelRow, 1);
            table.getModel().setValueAt(salaryAdvanceId, oldSelectedModelRow, 2);
        }
    }

    @Override
    public void read(List<SalaryAdvance> salaryaAvanceRecords) {
        // Clear the model every time, to append fresh results
        // and not accumulate on previous results
        model.setRowCount(0);

        if (salaryaAvanceRecords == null) {
            JOptionPane.showConfirmDialog(panelTable,
                    "Select Employee", "",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object[] modelRow = new Object[6];

        int size = salaryaAvanceRecords.size();
        for (int i = 0; i < size; i++) {
            SalaryAdvance salaryAdvance = salaryaAvanceRecords.get(i);
            modelRow[0] = salaryAdvance.getDateTaken();
            modelRow[1] = salaryAdvance.getAmount();
            modelRow[2] = salaryAdvance.getId();
            model.addRow(modelRow);
        }
    }

    public class LockableTableRowCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
            Component tableCellRendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Integer renderedRow = table.convertRowIndexToModel(row);
            Integer selectedRow = (Integer) table.getClientProperty(selectedRowKey);

            if (selectedRow != null && renderedRow.equals(selectedRow)) {
                setBackground(rowSelectionColor);
                setForeground(table.getForeground());
            } else if (!isSelected) {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
                setBorder(noFocusBorder);
            }
            return tableCellRendererComponent;
        }
    }

    public void addRowSelectedListener(RowSelectedListener rcl) {
        this.rowSelectedListeners.add(rcl);
    }

    private void notifyRowSelectedListener(int salaryadvanceId) {
        this.rowSelectedListeners.forEach((rcl) -> {
            rcl.rowSelectedWithRecordId(salaryadvanceId);
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

    public void addDeleteListener(DeleteListener deleteListener) {
        this.deleteListeners.add(deleteListener);
    }

    private void notifyDeleted() {
        this.deleteListeners.forEach((deleteListener) -> {
            deleteListener.deleted();
        });
    }

    private class RowSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent event) {

            if (!event.getValueIsAdjusting() && !boolEditMode) {
                // Ensure single event invoke
                DefaultListSelectionModel selectionModel = (DefaultListSelectionModel) event.getSource();

                if (selectionModel.isSelectionEmpty()) {
                    // Table row deselection occurred
                    boolRowSelected = false;
                    salaryAdvanceId = null;
                    table.setComponentPopupMenu(null);
                    notifyRowDeselection();
                } else {
                    boolRowSelected = true;
                    int viewRow = table.getSelectedRow();
                    if (viewRow > -1) {
                        int salaryAdvanceIdColumn = 2;
                        selectedModelRow = table.convertRowIndexToModel(viewRow);

                        Object salaryAdvanceIdObject = table.getModel().getValueAt(selectedModelRow, salaryAdvanceIdColumn);
                        salaryAdvanceId = Integer.parseInt(salaryAdvanceIdObject.toString());
                        notifyRowSelectedListener(salaryAdvanceId);
                        if (boolDisplayMode) {
                            table.setComponentPopupMenu(popupMenu);
                        }
                    }
                }
            }
        }
    }

    private class SelectRowOnMouseRightClick implements PopupMenuListener {

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            SwingUtilities.invokeLater(() -> {
                int rowAtPoint = table.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), table));
                if (rowAtPoint > -1) {
                    table.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                }
            });
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            // throw new UnsupportedOperationException
        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            //throw new UnsupportedOperationException
        }
    }

    private class MenuItemAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure to delete the selected Salary advance record?",
                    "Confirm", JOptionPane.YES_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (CRUDSalaryAdvance.delete(salaryAdvanceId)) {
                    model.removeRow(selectedModelRow);

                    notifyDeleted();

                    JOptionPane.showConfirmDialog(null,
                            "Salary advance deleted successfully",
                            "Info", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showConfirmDialog(null,
                            "Salary advance record was not found\n"
                            + "It might be deleted beforehand.",
                            "Info", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }
}
