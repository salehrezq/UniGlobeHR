package gui.performance;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;

/**
 *
 * @author Saleh
 */
public class PerformanceEdit implements
        PerformanceDisplayableListener,
        RowSelectedListener,
        RowDeselectedListener {

    private JButton btnEditMode;
    private List<PerformanceEditableListener> performanceEditableListeners;
    private boolean boolRowIsSelected;
    private boolean boolPerformanceDisplayable;

    public PerformanceEdit() {

        performanceEditableListeners = new ArrayList<>();
        btnEditMode = new JButton("Edit Mode");
        btnEditMode.setEnabled(false);
        btnEditMode.addActionListener(new EditPerformance());
        btnEditMode.setEnabled(false);
    }

    public JButton getBtnEditMode() {
        return btnEditMode;
    }

    @Override
    public void performanceDisplayable() {
        boolPerformanceDisplayable = true;
        if (boolRowIsSelected) {
            btnEditMode.setEnabled(true);
        }

    }

    @Override
    public void performanceUnDisplayable() {
        boolPerformanceDisplayable = false;
        btnEditMode.setEnabled(false);
    }

    @Override
    public void rowSelectedWithRecordId(int id) {
        boolRowIsSelected = true;
        if (boolPerformanceDisplayable) {
            btnEditMode.setEnabled(true);
        }
    }

    @Override
    public void rowDeselection() {
        boolRowIsSelected = false;
        btnEditMode.setEnabled(false);
    }

    class EditPerformance implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Performance editable");
            notifyPerformanceEditable();
        }
    }

    public void addPerformanceEditableListener(PerformanceEditableListener peditableListener) {
        this.performanceEditableListeners.add(peditableListener);
    }

    private void notifyPerformanceEditable() {
        this.performanceEditableListeners.forEach((peditableListener) -> {
            peditableListener.performanceEditable();
        });
    }

}
