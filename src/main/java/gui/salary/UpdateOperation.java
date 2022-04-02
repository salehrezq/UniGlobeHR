package gui.salaryadvance;

import datalink.CRUDSalaryAdvance;
import javax.swing.JButton;
import model.SalaryAdvance;

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
    public boolean post(SalaryAdvance salaryAdvance) {
        return CRUDSalaryAdvance.update(salaryAdvance);
    }

}
