/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.attendance;

import java.awt.Dimension;
import java.text.ParseException;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Saleh
 */
public class EmployeeAttendLate extends JPanel {

    private JCheckBox checkEmployeeLate;
    private JFormattedTextField minutesLate;

    public EmployeeAttendLate() {

        checkEmployeeLate = new JCheckBox();
        minutesLate = new JFormattedTextField(getMaskFormatter());
        minutesLate.setPreferredSize(new Dimension(30, 20));

        this.add(checkEmployeeLate);
        this.add(minutesLate);
    }

    /**
     * This method returns MaskFormatter that enforces 3 digits The # character
     * represent digit, and three of them (###) represents the allowed number of
     * digits.
     *
     * 000 is placeholder.
     *
     * @return MaskFormatter
     */
    private MaskFormatter getMaskFormatter() {
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter("###");
            mask.setPlaceholder("000");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mask;
    }

}
