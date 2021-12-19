package gui.performance;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Saleh
 */
public class PerformanceCancel
        implements
        PerformanceDisplayableListener,
        EditableListener {

    private JButton btnCancel;
    private PerformanceInput performanceInput;
    private List<CancelListener> cancelledListeners;

    public PerformanceCancel() {

        cancelledListeners = new ArrayList<>();
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

    @Override
    public void editable() {
        btnCancel.setEnabled(true);
    }

    public void addCancelListener(CancelListener canceller) {
        this.cancelledListeners.add(canceller);
    }

    private void notifyCancelled() {
        this.cancelledListeners.forEach((canceller) -> {
            canceller.cancelled();
        });
    }

    class SubmitPerformance implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            notifyCancelled();

            int dialogResult = JOptionPane.showConfirmDialog(null,
                    "You are about to clear all fields, sure?", "Warning", JOptionPane.YES_OPTION);

            if (dialogResult == JOptionPane.YES_OPTION) {
                performanceInput.clearInputFields();
            }
        }
    }

}
