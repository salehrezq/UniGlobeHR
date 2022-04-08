package gui.salary;

import model.Salary;

/**
 *
 * @author Saleh
 */
public interface Operation {

    public void switchOperationFor(Subject subject);

    public boolean post(Salary salary);
}
