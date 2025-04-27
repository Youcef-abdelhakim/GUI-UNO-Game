package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class SquaredButton extends Button {

    public SquaredButton(int size,Color color, String text,Color textColor) {
        super();
        setBGColor(color);
        setPreferredSize(new Dimension(size,size));
        setSize(size, size);
        setText(text);
        setForeground(textColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
    }
}
