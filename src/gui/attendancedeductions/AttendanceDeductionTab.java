/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendancedeductions;

import java.awt.GridBagLayout;
import javax.swing.JPanel;

/**
 *
 * @author Saleh
 */
public class AttendanceDeductionTab extends JPanel {

    private final GridBagLayout gridbag;

    public AttendanceDeductionTab() {
        super();
        this.gridbag = new GridBagLayout();
        this.setLayout(gridbag);
    }

}
