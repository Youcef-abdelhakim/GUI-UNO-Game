package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

public class Panel extends JPanel {

    private JScrollPane scrollPane;

    public Panel() {
        super();
        initDefaults();
    }

    public Panel(LayoutManager layout) {
        super(layout);
        initDefaults();
    }

    public Panel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        initDefaults();
    }

    public Panel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        initDefaults();
    }

    private void initDefaults() {
        this.setOpaque(false);
    }

    public void setBGColor(Color color) {
        this.setBackground(color);
        this.setOpaque(true);
    }

    @Override
    public void setBorder(Border border) {
        super.setBorder(border);
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        super.setPreferredSize(dimension);
    }

    public void setMargin(int top, int left, int bottom, int right) {
        this.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JScrollPane makeScrollable(int width, int height) {
        this.scrollPane = new JScrollPane(this);
        scrollPane.setPreferredSize(new Dimension(width, height));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        
        // Remove all extra space
        scrollPane.setViewportBorder(null);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        // Set fixed height for the panel
        this.setPreferredSize(new Dimension(width * 2, height)); // Wide enough for many cards
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, height)); // Prevent vertical growth
        this.setMinimumSize(new Dimension(width, height)); // Minimum size
        
        return scrollPane;
    }
}