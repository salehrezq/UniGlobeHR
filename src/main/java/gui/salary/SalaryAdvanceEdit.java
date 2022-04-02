package gui.salaryadvance;

import crud.UpdateICRPListener;
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
public class SalaryAdvanceEdit implements
        DisplayableListener,
        RowSelectedListener,
        RowDeselectedListener,
        CancelListener,
        UpdateListener,
        UpdateICRPListener {

    private JButton btnEditMode;
    private List<EditableListener> salaryAdvanceEditableListeners;
    private boolean boolRowIsSelected;
    private boolean boolSalaryAdvanceDisplayable;
    private boolean boolEditMode;

    public SalaryAdvanceEdit() {

        salaryAdvanceEditableListeners = new ArrayList<>();
        btnEditMode = new JButton("Edit Mode");
        btnEditMode.setEnabled(false);
        btnEditMode.addActionListener(new EditSalaryAdvance());
        btnEditMode.setEnabled(false);
    }

    public JButton getBtnEditMode() {
        return btnEditMode;
    }

    @Override
    public void displayable() {
        boolSalaryAdvanceDisplayable = true;
        if (boolRowIsSelected) {
            btnEditMode.setEnabled(true);
        }

    }

    @Override
    public void unDisplayable() {
        boolSalaryAdvanceDisplayable = false;
        btnEditMode.setEnabled(false);
    }

    @Override
    public void rowSelectedWithRecordId(int id) {
        boolRowIsSelected = true;
        if (boolSalaryAdvanceDisplayable && !boolEditMode) {
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
        if (boolRowIsSelected && boolSalaryAdvanceDisplayable) {
            btnEditMode.setEnabled(true);
        }
    }

    @Override
    public void updated() {
        if (boolSalaryAdvanceDisplayable && boolEditMode) {
            boolEditMode = false;
            btnEditMode.setEnabled(true);
        }
    }

    @Override
    public void updatedICRP() {
        boolRowIsSelected = false;
        boolEditMode = false;
        btnEditMode.setEnabled(false);
    }

    class EditSalaryAdvance implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolEditMode = true;
            btnEditMode.setEnabled(false);
            notifySalaryAdvanceEditable();
        }
    }

    public void addSalaryAdvanceEditableListener(EditableListener peditableListener) {
        this.salaryAdvanceEditableListeners.add(peditableListener);
    }

    private void notifySalaryAdvanceEditable() {
        this.salaryAdvanceEditableListeners.forEach((peditableListener) -> {
            peditableListener.editable();
        });
    }

}
