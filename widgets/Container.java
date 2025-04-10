package widgets;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.JPanel;

public class Container extends JPanel {
    public Container(LayoutManager layout,Dimension d,Color color){
        super(layout);
        this.setSize(d);
        this.setBackground(color);
        this.setVisible(true);
    }
    public Container(Dimension d,Color color){
        super();
        this.setSize(d);
        this.setBackground(color);
        this.setVisible(true);
    }
    public void adjustDimensionByLayout(Dimension d){
        this.setPreferredSize(d);
    }
}
