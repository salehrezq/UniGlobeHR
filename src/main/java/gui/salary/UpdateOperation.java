package gui.salary;

import datalink.CRUDSalary;
import javax.swing.JButton;
import model.Salary;

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
    public boolean post(Salary salary) {
        return CRUDSalary.update(salary);
    }

}
