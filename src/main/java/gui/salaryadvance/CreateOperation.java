package gui.salaryadvance;

import datalink.CRUDSalaryAdvance;
import javax.swing.JButton;
import model.SalaryAdvance;

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
    public boolean post(SalaryAdvance salaryAdvance) {
        return CRUDSalaryAdvance.create(salaryAdvance);
    }

}
