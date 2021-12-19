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
public class PerformanceEdit implements PerformanceDisplayableListener {

    private JButton btnEditMode;
    private List<PerformanceEditableListener> performanceEditableListeners;

    public PerformanceEdit() {

        performanceEditableListeners = new ArrayList<>();
        btnEditMode = new JButton("Edit Mode");
        btnEditMode.addActionListener(new EditPerformance());
        btnEditMode.setEnabled(false);
    }

    public JButton getBtnEditMode() {
        return btnEditMode;
    }

    @Override
    public void performanceDisplayable() {
        btnEditMode.setEnabled(true);
    }

    @Override
    public void performanceUnDisplayable() {
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
