package gui.salary;

import javax.swing.JPanel;

/**
 *
 * @author Saleh
 */
public class SalaryTab {

    private JPanel panel;
    private SalaryDetail salaryDetail;

    public SalaryTab() {

        salaryDetail = new SalaryDetail();
        panel = new JPanel();
        panel.add(salaryDetail.getContainer());
    }

    public JPanel getContainer() {
        return panel;
    }
}
