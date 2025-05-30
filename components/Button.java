package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JButton;
import util.Util;

public class Button extends JButton {
    public Button() {
        super();
        initDefaults();
    }

    public Button(String text) {
        super(text);
        initDefaults();
    }

    public Button(Icon icon) {
        super(icon);
        initDefaults();
    }

    public Button(String text, Icon icon) {
        super(text, icon);
        initDefaults();
    }

    public Button(Icon icon, Dimension dimension) {
        super(icon);
        this.setSize(dimension);
        initDefaults();
    }

    public Button(String text, Dimension dimension) {
        super(text);
        this.setPreferredSize(dimension);
        initDefaults();
    }

    private void initDefaults() {
        this.setFocusPainted(false);
        this.setVisible(true);
    }

    public void setBGColor(Color color) {
        this.setBackground(color);
    }

    public void adjustFont(Font font) {
        this.setFont(font);
    }

    public void onHoverBrighten(float brightnessAmount) {
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            Color current = getBackground();
    
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Util.lighten(current, brightnessAmount));
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(current);
            }
        });
    }
}