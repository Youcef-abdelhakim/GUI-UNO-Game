package components;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JSplitPane;

public class DivideContainer<T extends JComponent, V extends JComponent> extends JSplitPane {

    public static final int HORIZONTAL = JSplitPane.HORIZONTAL_SPLIT;
    public static final int VERTICAL = JSplitPane.VERTICAL_SPLIT;

    public DivideContainer(int orientation, T leftComponent, V rightComponent, double spaceOfLeft) {
        super(orientation, leftComponent, rightComponent);
        this.setResizeWeight(spaceOfLeft);
        this.setDividerSize(0);
        this.setOpaque(false); 
        this.setBorder(null); 
    }

    public void setBG(Color color){
        this.setBackground(color);
    }
}
