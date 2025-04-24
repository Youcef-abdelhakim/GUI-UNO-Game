package components;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;

public class Text extends JLabel{
    public Text(String text,Font font,Color color){
        super(text);
        this.setFont(font);
        this.setForeground(color);
    }
    public Text(String text){
        super(text);
    }
    
}
