package ui.views;

import components.Frame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.SwingUtilities;
import ui.widgets.MainPage.GameTitle;
import ui.widgets.MainPage.NewAndLoadButton;


public final class MainPage {
    public void createAndAdjustTitle(Frame mainFrame){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        GameTitle g = new GameTitle(new FlowLayout());
        g.setMargin(0, 0, 75, 0);
        mainFrame.addWidget(g,constraints);
    }
    public void createAndAdjustListOfButtons(Frame mainFrame){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        NewAndLoadButton n = new NewAndLoadButton();
        n.setMargin(0, 0, 100, 0); 
        mainFrame.addWidget(n,constraints);
    }
    
    public MainPage() {
        Frame mainFrame = new Frame("UNO!!", new Color(0x042e54), new Dimension(900,650),new GridBagLayout());
        createAndAdjustTitle(mainFrame);
        createAndAdjustListOfButtons(mainFrame);
        SwingUtilities.invokeLater(() -> {
            mainFrame.revalidate();
            mainFrame.repaint();
        });
    }

    public static void main(String[] args) {
        @SuppressWarnings("unused")
        MainPage main = new MainPage();
    }
}
