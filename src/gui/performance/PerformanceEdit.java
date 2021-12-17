package gui.performance;

import javax.swing.JButton;

/**
 *
 * @author Saleh
 */
public class PerformanceEdit {

    private JButton btnEditMode;

    public PerformanceEdit() {

        btnEditMode = new JButton("Edit Mode");
        btnEditMode.setEnabled(false);  
    }

    public JButton getBtnEditMode() {
        return btnEditMode;
    }
    
    

}
