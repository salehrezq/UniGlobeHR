package gui.performance;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Saleh
 */
public class PerformanceCancel
        implements
        PerformanceDisplayableListener {

    private JButton btnCancel;
    private PerformanceInput performanceInput;

    public PerformanceCancel() {

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new SubmitPerformance());
    }

    public void setPerformanceInput(PerformanceInput performanceInput) {
        this.performanceInput = performanceInput;
    }

    public JButton getButtonCancel() {
        return btnCancel;
    }

    @Override
    public void performanceDisplayable() {
        btnCancel.setEnabled(false);
    }

    @Override
    public void performanceUnDisplayable() {
        btnCancel.setEnabled(true);
    }

    class SubmitPerformance implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            int dialogResult = JOptionPane.showConfirmDialog(null,
                    "You are about to clear all fields, sure?", "Warning", JOptionPane.YES_OPTION);

            if (dialogResult == JOptionPane.YES_OPTION) {
                performanceInput.clearInputFields();
            }
        }
    }

}
