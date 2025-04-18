package ui.views;

import components.Container;
import components.DivideContainer;
import components.Frame;
import components.LogoPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.SwingUtilities;
import ui.widgets.MainPage.GameTitle;
import ui.widgets.MainPage.NewAndLoadButton;

public final class MainPage {

    public void createAndAdjustTitle(Container conatiner) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        GameTitle g = new GameTitle(new FlowLayout(FlowLayout.CENTER, 0, 0));
        g.setMargin(0, 0, 75, 0);
        g.setOpaque(false);
        conatiner.addWidget(g, constraints);
    }

    public void createAndAdjustListOfButtons(Container conatiner) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        NewAndLoadButton n = new NewAndLoadButton();
        n.setMargin(0, 0, 100, 0);
        n.setOpaque(false);
        conatiner.addWidget(n, constraints);
    }

    public void createAndAdjustTopLeftLogo(Container conatiner) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(0, 0, 50, 0);

        LogoPanel leftLogo = new LogoPanel("/src/images/changeDirection-ofgame-symble.png", new Dimension(120, 120));
        leftLogo.setOpaque(false);

        conatiner.addWidget(leftLogo, constraints);
    }

    public void createAndAdjustBottomLeftLogo(Container conatiner) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(50, 0, 0, 0);

        LogoPanel leftLogo = new LogoPanel("/src/images/4cards.png", new Dimension(120, 120));
        leftLogo.setOpaque(false);

        conatiner.addWidget(leftLogo, constraints);
    }

    public void createAndAdjustTopRightLogo(Container conatiner) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(0, 0, 50, 0);
        LogoPanel rightLogo = new LogoPanel("/src/images/cantPlay-symbole.png", new Dimension(120, 120));
        rightLogo.setOpaque(false);
        conatiner.addWidget(rightLogo, constraints);
    }

    public void createAndAdjustBottomRightLogo(Container conatiner) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(50, 0, 0, 0);
        LogoPanel rightLogo = new LogoPanel("/src/images/2cards-removebg-preview.png", new Dimension(120, 120));
        rightLogo.setOpaque(false);

        conatiner.addWidget(rightLogo, constraints);
    }

    public MainPage() {
        Frame mainFrame = new Frame("UNO!!", new Color(0x042e54), new Dimension(900, 650), new GridBagLayout());
        Container centerContainer = new Container();
        centerContainer.setLayout(new GridBagLayout());
        Container leftContainer = new Container();
        leftContainer.setLayout(new GridBagLayout());

        Container rightContainer = new Container();
        rightContainer.setLayout(new GridBagLayout());

        createAndAdjustTitle(centerContainer);
        createAndAdjustListOfButtons(centerContainer);
        createAndAdjustTopLeftLogo(leftContainer);
        createAndAdjustTopRightLogo(rightContainer);
        createAndAdjustBottomLeftLogo(leftContainer);
        createAndAdjustBottomRightLogo(rightContainer);
        leftContainer.setOpaque(false);
        centerContainer.setOpaque(false);
        rightContainer.setOpaque(false);
        leftContainer.setMargin(0, 0, 0, 100);
        rightContainer.setMargin(0, 100, 0, 0);
        DivideContainer<Container, Container> centerAndRight = new DivideContainer<>(DivideContainer.HORIZONTAL, centerContainer, rightContainer, 0.67f);
        DivideContainer<Container, DivideContainer<Container, Container>> mainContainer = new DivideContainer<>(DivideContainer.HORIZONTAL, leftContainer, centerAndRight, 0.33f);
        centerAndRight.setOpaque(false);
        mainContainer.setOpaque(false);
        GridBagConstraints mainConstraints = new GridBagConstraints();
        mainConstraints.gridx = 0;
        mainConstraints.gridy = 0;
        mainConstraints.anchor = GridBagConstraints.CENTER;
        mainFrame.addWidget(mainContainer, mainConstraints);
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
