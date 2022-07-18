package gui.salary;

import datalink.CRUDSalary;
import javax.swing.JButton;
import model.Salary;

/**
 *
 * @author Saleh
 */
public class DeleteOperation implements Operation {

    private Subject subject;
    private JButton btnDelete;

    @Override
    public void switchOperationFor(Subject subject) {
        this.subject = subject;
        btnDelete = this.subject.getOperationButton();
        btnDelete.setText("Delete");
    }

    @Override
    public boolean post(Salary salary) {
        return CRUDSalary.delete(salary);
    }

}
