package gui.performance;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Saleh
 */
public class PerformanceInput {

    private JPanel panelInputs;
    private JTextField tfTitle;
    private JScrollPane scrollableTextArea;
    private JTextArea taDescription;

    public PerformanceInput() {

        panelInputs = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        tfTitle = new JTextField();
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 30, 5, 30);
        c.weightx = 1.0;
        panelInputs.add(tfTitle, c);

        c = new GridBagConstraints();
        taDescription = new JTextArea();
        scrollableTextArea = new JScrollPane(taDescription);
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 30, 5, 30);
        c.weightx = 1.0;
        c.weighty = 1.0;
        panelInputs.add(scrollableTextArea, c);
    }

    public JPanel getPanelInputs() {
        return panelInputs;
    }

}
