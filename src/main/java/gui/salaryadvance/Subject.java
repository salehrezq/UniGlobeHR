package gui.salaryadvance;

import javax.swing.JButton;

/**
 *
 * @author Saleh
 */
public interface Subject {

    public void setOperation(Operation operation);

    public JButton getOperationButton();

    public Operation getOperation();
}
