package components;

import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JButton;

public class Button extends JButton {

    public Button(Icon icon, Dimension dimension) {
        super(icon);
        this.setSize(dimension);
        this.setVisible(true);
    }

    public Button(String text, Dimension dimension) {
        super(text);
        this.setText(text);
        this.setPreferredSize(dimension);
        this.setVisible(true);
    }

}
