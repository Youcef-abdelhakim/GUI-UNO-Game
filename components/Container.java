package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class Container extends JPanel {

    public Container() {
        super();
        this.setVisible(true);
    }

    public Container(LayoutManager layout, Dimension d, Color color) {
        super(layout);
        this.setSize(d);
        this.setBackground(color);
        this.setVisible(true);
    }

    public Container(Dimension d, Color color) {
        super();
        this.setSize(d);
        this.setBackground(color);
        this.setVisible(true);
    }

    public void adjustDimensionByLayout(Dimension d) {
        this.setPreferredSize(d);
    }

    public void addWidget(JComponent c) {
        this.add(c);
    }

    public void addWidget(JComponent c, String constraint) {
        this.add(c, constraint);
    }

    public void setColor(Color color) {
        this.setBackground(color);
    }

    public void addLayout(LayoutManager lm) {
        this.setLayout(lm);
    }

    public void makeBgTransparent() {
        this.setOpaque(false);
    }

    public void setMargin(int top, int left, int bottom, int right) {
        this.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }
}
