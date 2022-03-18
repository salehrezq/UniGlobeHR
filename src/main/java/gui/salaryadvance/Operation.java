package gui.salaryadvance;

import model.Performance;

/**
 *
 * @author Saleh
 */
public interface Operation {

    public void switchOperationFor(Subject subject);

    public boolean post(Performance performance);
}
