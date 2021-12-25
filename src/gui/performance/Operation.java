package gui.performance;

import model.Performance;

/**
 *
 * @author Saleh
 */
public interface Operation {

    public void switchOperationFor(Subject subject);

    public void updateGUI();

    public Integer post(Performance performance);
}
