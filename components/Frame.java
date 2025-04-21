package components;

import java.awt.*;
import javax.swing.*;

public class Frame extends JFrame {

    public Frame(String title, Color bg, Dimension d, LayoutManager layoutManager) {
        super(title);
        this.setSize(d);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(bg);
        this.setLayout(layoutManager != null ? layoutManager : new FlowLayout());
        this.setVisible(true);
    }

    public void addWidget(JComponent widget) {
        this.add(widget);
    }

    public void addWidget(JComponent widget, Object constraints) {
        this.add(widget, constraints);
    }

}
