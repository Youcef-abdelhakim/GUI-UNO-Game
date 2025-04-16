package components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private final Image image;
    public ImagePanel(String path,Dimension D) {
        this.image = new ImageIcon(path).getImage();
        this.setPreferredSize(D);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // scales to fit
    }
}