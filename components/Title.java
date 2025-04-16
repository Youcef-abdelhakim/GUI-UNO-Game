package components;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;

public class Title extends JLabel{
    public Title(String text,Font font,Color color){
        super(text);
        this.setFont(font);
        this.setForeground(color);
    }
    
}
