package utilities.imagefilechooser;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import net.coobird.thumbnailator.Thumbnails;

/**
 *
 * @author Saleh
 */
public class IMGFileChooser implements ActionListener {

    private Component parent;
    private JFileChooser fileChooser;
    private byte[] photoInBytes;
    private Preferences prefs;
    private static final String LAST_USED_FOLDER = "lastusedfolder";

    public void setParentComponent(Component parent) {
        this.parent = parent;
    }

    public byte[] getPhotoInBytes() {
        return photoInBytes;
    }

    public void setPhotoInBytes(byte[] aPhotoInBytes) {
        photoInBytes = aPhotoInBytes;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (fileChooser == null) {
            prefs = Preferences.userRoot().node(getClass().getName());
            fileChooser = new JFileChooser(prefs.get(LAST_USED_FOLDER, new File(".").getAbsolutePath()));
            fileChooser.addChoosableFileFilter(new ImageFilter());
            fileChooser.setAcceptAllFileFilterUsed(false);
        }

        int returnedValue = fileChooser.showDialog(parent, "Select employee image");

        if (returnedValue == JFileChooser.APPROVE_OPTION) {
            try {
                File f = fileChooser.getSelectedFile();
                BufferedImage originalImage = ImageIO.read(f);
                BufferedImage thumbnail = Thumbnails.of(originalImage)
                        .size(100, 100)
                        .asBufferedImage();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(thumbnail, "jpg", baos);
                baos.flush();
                photoInBytes = baos.toByteArray();
                prefs.put(LAST_USED_FOLDER, fileChooser.getSelectedFile().getParent());
            } catch (IOException ex) {
                Logger.getLogger(IMGFileChooser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
