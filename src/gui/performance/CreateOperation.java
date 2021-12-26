package gui.performance;

import datalink.CRUDPerformance;
import javax.swing.JButton;
import model.Performance;

/**
 *
 * @author Saleh
 */
public class CreateOperation implements Operation {

    private Subject subject;
    private JButton btnCreate;

    @Override
    public void switchOperationFor(Subject subject) {
        this.subject = subject;
        btnCreate = this.subject.getOperationButton();
        btnCreate.setText("Create");
    }

    @Override
    public boolean post(Performance performance) {
        return CRUDPerformance.create(performance);
    }

}
