package components;

import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame(String title, Color bg, Dimension d, LayoutManager layoutManager) {
        super(title);
        this.setSize(d);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(bg);
        this.setLayout(null);
        this.setVisible(true);
    }

    public void addWidget(JComponent widget) {
        this.add(widget);
    }

    public void addWidget(JComponent widget, Object constraints) {
        this.add(widget, constraints);
    }

    // In your GameFrame class
    public void addWidget(Component component) {  // Changed from JComponent to Component
        this.add(component);
    }

    public void addWidget(Component component, Object constraints) {
        this.add(component, constraints);
    }
}
