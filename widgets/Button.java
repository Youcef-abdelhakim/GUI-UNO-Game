package widgets;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

public class Button extends JButton {
    
    public Button(String text, Color bgColor, Color frColor, int height, int width) {
        super(text);
        initializeButton(bgColor, frColor, height, width);
    }

    private void initializeButton(Color bgColor, Color frColor, int height, int width){
        setBackground(bgColor);
        setForeground(frColor);
        setPreferredSize(new java.awt.Dimension(width,height));
        setFocusPainted(false);
        setFont(new Font("Arial", Font.BOLD, 14));
        setBorderPainted(false);
    }

}
