package ui.views;

import components.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.SwingUtilities;
import util.*;

public class MainPage {

    public MainPage() {
        Frame main = new Frame("Uno!!", Color.GRAY, new Dimension(900, 650), new BorderLayout());
        Container left = new Container(new BorderLayout(), new Dimension(100, 10), new Color(0x4FD67A));
        Container right = new Container(new FlowLayout(), new Dimension(100, 10), new Color(0x89D2DC));
        Container topLeft = new Container();
        Container centerLeft = new Container();
        centerLeft.addLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        topLeft.makeBgTransparent();
        topLeft.setMargin(50, 0, 0, 0);
        topLeft.addWidget(new Title("UNO!!!", Util.loadCustomFont("src\\fonts\\Title Font\\Game Bubble.ttf", 75), Color.white));
        centerLeft.addWidget(new Button("New Game", new Dimension(150, 25)));
        centerLeft.makeBgTransparent();
        centerLeft.setMargin(175, 0, 0, 0);
        left.add(topLeft, BorderLayout.NORTH);
        left.add(centerLeft, BorderLayout.CENTER);
        right.addWidget(new ImagePanel("src\\images\\UNO!!.png", new Dimension(400, 400)));
        DivideContainer<Container> mainContainer = new DivideContainer<>(DivideContainer.HORIZONTAL, left, right, 0.33);
        main.addWidget(mainContainer);
        SwingUtilities.invokeLater(() -> {
            main.revalidate();
            main.repaint();
            mainContainer.setDividerLocation(0.4); // now it works
        });
    }

    public static void main(String[] args) {
        new MainPage();
    }
}
