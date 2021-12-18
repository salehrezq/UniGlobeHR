package gui.performance;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author Saleh
 */
public class PerformanceEdit implements PerformanceDisplayableListener {

    private JButton btnEditMode;

    public PerformanceEdit() {

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

        }
    }

}
