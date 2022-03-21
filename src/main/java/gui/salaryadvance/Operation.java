package gui.salaryadvance;

import model.SalaryAdvance;

/**
 *
 * @author Saleh
 */
public interface Operation {

    public void switchOperationFor(Subject subject);

    public boolean post(SalaryAdvance salaryAdvance);
}
