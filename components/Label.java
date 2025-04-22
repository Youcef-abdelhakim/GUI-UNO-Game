package components;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class Label extends JLabel {
    public static final int LEFT = JLabel.LEFT;
    public static final int CENTER = JLabel.CENTER;
    public static final int RIGHT = JLabel.RIGHT;
    
    public Label() {
        super();
        initDefaults();
    }

    public Label(String text) {
        super(text);
        initDefaults();
    }

    public Label(String text, int alignment) {
        super(text, alignment);
        initDefaults();
    }

    public Label(Icon image) {
        super(image);
        initDefaults();
    }

    public Label(Icon image, int alignment) {
        super(image, alignment);
        initDefaults();
    }

    public Label(String text, Icon icon, int alignment) {
        super(text, icon, alignment);
        initDefaults();
    }

    private void initDefaults() {
        this.setOpaque(false);
    }

    public void setTextColor(Color color) {
        this.setForeground(color);
    }

    public void adjustFont(Font font) {
        this.setFont(font);
    }

    public void setBorder(Border border) {
        super.setBorder(border);
    }

    public void setMargin(int top, int left, int bottom, int right) {
        this.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }
}