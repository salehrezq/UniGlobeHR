package gui.performance;

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
    public Integer post(Performance performance) {
        System.out.println("do update");
        return 0;
    }

}
