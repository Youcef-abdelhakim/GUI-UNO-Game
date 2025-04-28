package components;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Text extends JLabel{
    public Text(String text,Font font,Color color){
        super(text,SwingConstants.CENTER);
        this.setFont(font);
        this.setForeground(color);
    }
    public Text(String text){
        super(text,SwingConstants.CENTER);
    }
    
}
