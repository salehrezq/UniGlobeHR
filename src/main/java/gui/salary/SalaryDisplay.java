package gui.salary;

import crud.UpdateICRPListener;
import crud.UpdateListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Saleh
 */
public class SalaryDisplay
        implements
        EditableListener,
        CancelListener,
        UpdateListener,
        UpdateICRPListener {

    private SalaryInput salaryInput;
    private JCheckBox checkDisplayMode;
    private List<DisplayableListener> salaryDisplayableListeners;

    public SalaryDisplay() {

        salaryDisplayableListeners = new ArrayList<>();

        checkDisplayMode = new JCheckBox("Display Mode");
        checkDisplayMode.addItemListener(new ChechBoxListener());
    }

    public void setSalaryInput(SalaryInput salaryInput) {
        this.salaryInput = salaryInput;
    }

    public JCheckBox getCheckDisplayMode() {
        return checkDisplayMode;
    }

    public void addSalaryDisplayableListener(DisplayableListener sadl) {
        this.salaryDisplayableListeners.add(sadl);
    }

    private void notifySalaryDisplayable() {
        this.salaryDisplayableListeners.forEach((sadl) -> {
            sadl.displayable();
        });
    }

    private void notifySalaryUnDisplayable() {
        this.salaryDisplayableListeners.forEach((sadl) -> {
            sadl.unDisplayable();
        });
    }

    @Override
    public void editable() {
        checkDisplayMode.setEnabled(false);
    }

    @Override
    public void cancelled() {
        checkDisplayMode.setEnabled(true);
    }

    @Override
    public void updated() {
        checkDisplayMode.setEnabled(true);
    }

    @Override
    public void updatedICRP() {
        checkDisplayMode.setEnabled(true);
    }

    private class ChechBoxListener implements ItemListener {

        // https://stackoverflow.com/q/70354055/6811102
        // count is used to assist in checking when to display
        // the confirmation dialog box.
        int count = 0;

        @Override
        public void itemStateChanged(ItemEvent event) {

            Object source = event.getSource();

            if (source == checkDisplayMode) {

                if (event.getStateChange() == ItemEvent.SELECTED) {

                    count += 1;
                    if (count == 1) {
                        int dialogResult = JOptionPane.showConfirmDialog(null,
                                "To display descriptions; input fields\n"
                                + "will be cleared and disabled, sure?",
                                "Warning", JOptionPane.YES_OPTION);

                        if (dialogResult == JOptionPane.YES_OPTION) {
                            notifySalaryDisplayable();
                            count += 1;
                            checkDisplayMode.setSelected(true);
                        }
                    }
                } else if (event.getStateChange() == ItemEvent.DESELECTED && count == 0) {
                    notifySalaryUnDisplayable();
                }
            }
            count = 0;
        }
    }

}
