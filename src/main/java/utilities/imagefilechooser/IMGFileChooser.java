package utilities.imagefilechooser;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;

/**
 *
 * @author Saleh
 */
public class IMGFileChooser implements ActionListener {

    private Component parent;
    private JFileChooser fileChooser;

    public void setParentComponent(Component parent) {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new ImageFilter());
            fileChooser.setAcceptAllFileFilterUsed(false);

        }
        fileChooser.showDialog(parent, "Select employee image");
    }
}
