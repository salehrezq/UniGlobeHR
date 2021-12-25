package gui.performance;

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
    }

    @Override
    public void updateGUI() {
        btnCreate = subject.getOperationButton();
        btnCreate.setText("Create");
    }

    @Override
    public Integer post(Performance performance) {
        System.out.println("do create");
        return 0;
    }

}
