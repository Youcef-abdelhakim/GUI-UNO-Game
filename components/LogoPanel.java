package components;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class LogoPanel extends JPanel {

    public LogoPanel(String imagePath, Dimension size) {
        setOpaque(false);
        setLayout(new FlowLayout());

        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl == null) {
            JLabel errorLabel = new JLabel("Image not found!");
            errorLabel.setForeground(Color.RED);
            add(errorLabel);
            return;
        }

        ImageIcon icon = new ImageIcon(imageUrl);
        if (size != null) {
            Image scaledImage = icon.getImage().getScaledInstance(
                    size.width, size.height, Image.SCALE_SMOOTH
            );
            icon = new ImageIcon(scaledImage);
        }
        JLabel logo = new JLabel(icon);
        add(logo);
    }
}
