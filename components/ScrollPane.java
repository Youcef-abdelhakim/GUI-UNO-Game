package components;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JScrollPane;

public class ScrollPane extends JScrollPane {
    
    public ScrollPane() {
        super();
        initialize();
    }
    
    public ScrollPane(Component view) {
        super(view);
        initialize();
    }
    
    public ScrollPane(int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
        initialize();
    }
    
    public ScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        initialize();
    }
    
    private void initialize() {
        this.getVerticalScrollBar().setUnitIncrement(16);
        this.getHorizontalScrollBar().setUnitIncrement(16);
        this.setBorder(null);
    }
    
    public ScrollPane withPreferredSize(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        return this;
    }
    
    public ScrollPane withScrollBarPolicies(int verticalPolicy, int horizontalPolicy) {
        this.setVerticalScrollBarPolicy(verticalPolicy);
        this.setHorizontalScrollBarPolicy(horizontalPolicy);
        return this;
    }
}
