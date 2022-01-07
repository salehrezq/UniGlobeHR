package gui.performance;

import crud.UpdateListener;
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
        DisplayableListener,
        RowSelectedListener,
        RowDeselectedListener,
        CancelListener,
        UpdateListener {

    private JButton btnEditMode;
    private List<EditableListener> performanceEditableListeners;
    private boolean boolRowIsSelected;
    private boolean boolPerformanceDisplayable;
    private boolean boolEditMode;

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
    public void displayable() {
        boolPerformanceDisplayable = true;
        if (boolRowIsSelected) {
            btnEditMode.setEnabled(true);
        }

    }

    @Override
    public void unDisplayable() {
        boolPerformanceDisplayable = false;
        btnEditMode.setEnabled(false);
    }

    @Override
    public void rowSelectedWithRecordId(int id) {
        boolRowIsSelected = true;
        if (boolPerformanceDisplayable && !boolEditMode) {
            btnEditMode.setEnabled(true);
        }
    }

    @Override
    public void rowDeselection() {
        boolRowIsSelected = false;
        btnEditMode.setEnabled(false);
    }

    @Override
    public void cancelled() {
        boolEditMode = false;
        if (boolRowIsSelected && boolPerformanceDisplayable) {
            btnEditMode.setEnabled(true);
        }
    }

    @Override
    public void updated() {
        if (boolPerformanceDisplayable && boolEditMode) {
            boolEditMode = false;
            btnEditMode.setEnabled(true);
        }
    }

    class EditPerformance implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolEditMode = true;
            btnEditMode.setEnabled(false);
            notifyPerformanceEditable();
        }
    }

    public void addPerformanceEditableListener(EditableListener peditableListener) {
        this.performanceEditableListeners.add(peditableListener);
    }

    private void notifyPerformanceEditable() {
        this.performanceEditableListeners.forEach((peditableListener) -> {
            peditableListener.editable();
        });
    }

}
