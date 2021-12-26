package gui.performance;

import datalink.CRUDPerformance;
import javax.swing.JButton;
import model.Performance;

/**
 *
 * @author Saleh
 */
public class UpdateOperation implements Operation {

    private Subject subject;
    private JButton btnUpdate;

    @Override
    public void switchOperationFor(Subject subject) {
        this.subject = subject;
    }

    @Override
    public void updateGUI() {
        btnUpdate = subject.getOperationButton();
        btnUpdate.setText("Update");
    }

    @Override
    public boolean post(Performance performance) {
        return CRUDPerformance.update(performance);
    }

}
