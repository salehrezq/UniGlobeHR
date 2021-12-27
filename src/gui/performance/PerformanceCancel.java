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
public class PerformanceCancel
        implements
        PerformanceDisplayableListener,
        EditableListener,
        PerformanceSubmittedListener {

    private JButton btnCancel;
    private List<CancelListener> cancelledListeners;
    private boolean booleditMode;

    public PerformanceCancel() {

        cancelledListeners = new ArrayList<>();
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new SubmitPerformance());

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
        booleditMode = true;
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

    @Override
    public void created() {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updated() {
        if (booleditMode) {
            btnCancel.setEnabled(false);
        }
    }

    class SubmitPerformance implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            notifyCancelled();
            if (booleditMode) {
                btnCancel.setEnabled(false);
                booleditMode = false;
            }
        }
    }
}
