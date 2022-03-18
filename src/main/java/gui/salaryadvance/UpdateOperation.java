package gui.salaryadvance;

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
        btnUpdate = this.subject.getOperationButton();
        btnUpdate.setText("Update");
    }

    @Override
    public boolean post(Performance performance) {
        return CRUDPerformance.update(performance);
    }

}
