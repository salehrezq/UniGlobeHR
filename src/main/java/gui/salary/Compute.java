package gui.salary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author Saleh
 */
public class Compute {

    private JButton btnCompute;

    public Compute() {

        btnCompute = new JButton("Compute");
        btnCompute.addActionListener(new ComputePayables());
    }

    public JButton getBtnCompute() {
        return btnCompute;
    }

    class ComputePayables implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("compute place holder");
        }
    }

}
