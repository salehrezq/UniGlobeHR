package gui.performance;

import crud.DeleteListener;
import crud.ReadListener;
import datalink.CRUDPerformance;
import datalink.CRUDPerformanceType;
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
import model.Performance;

/**
 *
 * @author Saleh
 */
public class PerformanceRecords
        implements
        EmployeeSelectedListener,
        PerformanceDisplayableListener,
        EditableListener,
        CancelListener,
        PerformanceSubmittedListener,
        ReadListener<Performance> {

    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollTable;
    private JPanel panelTable;
    GridBagConstraints c;
    private List<RowSelectedListener> rowSelectedListeners;
    private List<RowDeselectedListener> rowDeselectedListeners;
    private List<DeleteListener> deleteListeners;
    private boolean boolRowSelected;
    private Integer performanceId;
    private boolean boolEditMode;
    private boolean boolDisplayMode;
    private final String selectedRowKey = "selectedRow";
    private Integer selectedModelRow;
    private Integer oldSelectedModelRow;
    private final Color rowSelectionColor;
    private final JPopupMenu popupMenu;
    private final JMenuItem menuItemDelete;

    public PerformanceRecords() {
        super();

        rowSelectedListeners = new ArrayList<>();
        rowDeselectedListeners = new ArrayList<>();
        deleteListeners = new ArrayList<>();

        panelTable = new JPanel();

        model = new DefaultTableModel(new String[]{"DateTime", "State", "Type", "Amount", "Title", "Performance Id"}, 0) {
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
    public void performanceDisplayable() {

        if (boolRowSelected) {
            table.setComponentPopupMenu(popupMenu);
        }
        boolDisplayMode = true;

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
        table.setComponentPopupMenu(null);
        boolDisplayMode = false;
        table.getSelectionModel().setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    @Override
    public void editable() {
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
    public void created() {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updated() {
        restoreRowSelection();

        Performance performance = CRUDPerformance.getById(performanceId);

        table.getModel().setValueAt(performance.getDateTime(), oldSelectedModelRow, 0);
        table.getModel().setValueAt(performanceState(performance.getState()), oldSelectedModelRow, 1);
        table.getModel().setValueAt(getTypeText(performance.getTypeId()), oldSelectedModelRow, 2);
        table.getModel().setValueAt(performance.getAmount(), oldSelectedModelRow, 3);
        table.getModel().setValueAt(performance.getTitle(), oldSelectedModelRow, 4);
        table.getModel().setValueAt(performanceId, oldSelectedModelRow, 5);
    }

    @Override
    public void read(List<Performance> performancesRecords) {
        // Clear the model every time, to append fresh results
        // and not accumulate on previous results
        model.setRowCount(0);

        if (performancesRecords == null) {
            JOptionPane.showConfirmDialog(panelTable,
                    "Select Employee", "",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object[] modelRow = new Object[6];

        int size = performancesRecords.size();
        for (int i = 0; i < size; i++) {
            Performance performance = performancesRecords.get(i);
            modelRow[0] = performance.getDateTime();
            modelRow[1] = performanceState(performance.getState());
            modelRow[2] = getTypeText(performance.getTypeId());
            modelRow[3] = performance.getAmount();
            modelRow[4] = performance.getTitle();
            modelRow[5] = performance.getId();
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

    private String performanceState(boolean state) {

        String stateType = "Undefined";

        if (state) {
            stateType = "Positive";
        } else {
            stateType = "Negative";
        }
        return stateType;
    }

    private String getTypeText(int typeID) {
        return CRUDPerformanceType.getById(typeID).getType();
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
                    performanceId = null;
                    table.setComponentPopupMenu(null);
                    notifyRowDeselection();
                } else {
                    boolRowSelected = true;
                    int viewRow = table.getSelectedRow();
                    if (viewRow > -1) {
                        int performanceIdColumn = 5;
                        selectedModelRow = table.convertRowIndexToModel(viewRow);

                        Object performanceIdObject = table.getModel().getValueAt(selectedModelRow, performanceIdColumn);
                        performanceId = Integer.parseInt(performanceIdObject.toString());
                        notifyRowSelectedListener(performanceId);
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
                    "Are you sure to delete the selected performance record?",
                    "Confirm", JOptionPane.YES_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (CRUDPerformance.delete(performanceId)) {
                    model.removeRow(selectedModelRow);

                    notifyDeleted();

                    JOptionPane.showConfirmDialog(null,
                            "Performance deleted successfully",
                            "Info", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showConfirmDialog(null,
                            "Performance record was not found\n"
                            + "It might be deleted beforehand.",
                            "Info", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }
}
