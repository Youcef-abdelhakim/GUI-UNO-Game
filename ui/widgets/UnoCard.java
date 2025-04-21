package ui.widgets;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import components.Container;

public class UnoCard extends Container {
    private final String color;
    private final String value;
    private boolean selected = false;

    public String getColor() {
        return color;
    }

    public String getValue() {
        return value;
    }

    public UnoCard(String color, String value) {
        super();
        this.color = color;
        this.value = value;

        setPreferredSize(new Dimension(80, 120));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selected = !selected;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Determine card background color
        Color bgColor;
        bgColor = switch (color.toLowerCase()) {
            case "red" -> Color.RED;
            case "blue" -> Color.BLUE;
            case "green" -> Color.GREEN;
            case "yellow" -> Color.YELLOW;
            default -> Color.BLACK;
        };

        // Draw card background
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // Draw permanent white inner border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 20, 20);

        // Draw selection border if selected
        if (selected) {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        }

        // Draw value text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(value);
        g2.drawString(value, (getWidth() - textWidth) / 2, getHeight() / 2 + 10);
    }
}
