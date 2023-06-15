package gui.empinsertdialoge;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

/**
 *
 * @author Saleh
 */
public class RotatablePreview extends JLabel {

    private BufferedImage master;
    private BufferedImage rotated;
    private double angle;

    public RotatablePreview() {
        super();
        angle = 0.0;
    }

    public void setImage(BufferedImage recivedImage) {
        master = recivedImage;
        rotated = master;
        repaint();
    }

    public byte[] getImageBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ImageIO.write(rotated, "jpg", baos);
        } catch (IOException ex) {
            Logger.getLogger(RotatablePreview.class.getName()).log(Level.SEVERE, null, ex);
        }
        return baos.toByteArray();
    }

    public void rotate() {
        angle = angle + 90.0;
        if (angle >= 360) {
            angle = 0.0;
        }
        rotated = rotateImageByDegrees(master, angle);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (rotated != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            int x = (getWidth() - rotated.getWidth()) / 2;
            int y = (getHeight() - rotated.getHeight()) / 2;
            g2d.drawImage(rotated, x, y, this);
            g2d.dispose();
        }
    }

    public BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, this);
        g2d.dispose();

        return rotated;
    }

}
