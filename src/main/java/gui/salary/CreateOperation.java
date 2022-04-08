package gui.salary;

import datalink.CRUDSalary;
import javax.swing.JButton;
import model.Salary;

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
    public boolean post(Salary salary) {
        return CRUDSalary.create(salary);
    }

}
