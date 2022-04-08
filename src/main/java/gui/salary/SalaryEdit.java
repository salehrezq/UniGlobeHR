package gui.salary;

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
public class SalaryEdit implements
        DisplayableListener,
        RowSelectedListener,
        RowDeselectedListener,
        CancelListener,
        UpdateListener,
        UpdateICRPListener {

    private JButton btnEditMode;
    private List<EditableListener> salaryEditableListeners;
    private boolean boolRowIsSelected;
    private boolean boolSalaryDisplayable;
    private boolean boolEditMode;

    public SalaryEdit() {

        salaryEditableListeners = new ArrayList<>();
        btnEditMode = new JButton("Edit Mode");
        btnEditMode.setEnabled(false);
        btnEditMode.addActionListener(new EditSalary());
        btnEditMode.setEnabled(false);
    }

    public JButton getBtnEditMode() {
        return btnEditMode;
    }

    @Override
    public void displayable() {
        boolSalaryDisplayable = true;
        if (boolRowIsSelected) {
            btnEditMode.setEnabled(true);
        }

    }

    @Override
    public void unDisplayable() {
        boolSalaryDisplayable = false;
        btnEditMode.setEnabled(false);
    }

    @Override
    public void rowSelectedWithRecordId(int id) {
        boolRowIsSelected = true;
        if (boolSalaryDisplayable && !boolEditMode) {
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
        if (boolRowIsSelected && boolSalaryDisplayable) {
            btnEditMode.setEnabled(true);
        }
    }

    @Override
    public void updated() {
        if (boolSalaryDisplayable && boolEditMode) {
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

    class EditSalary implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolEditMode = true;
            btnEditMode.setEnabled(false);
            notifySalaryEditable();
        }
    }

    public void addSalaryEditableListener(EditableListener peditableListener) {
        this.salaryEditableListeners.add(peditableListener);
    }

    private void notifySalaryEditable() {
        this.salaryEditableListeners.forEach((peditableListener) -> {
            peditableListener.editable();
        });
    }

}
