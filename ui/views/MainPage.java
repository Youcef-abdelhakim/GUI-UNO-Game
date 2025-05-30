package ui.views;

import components.Container;
import components.DivideContainer;
import components.LogoPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.SwingUtilities;
import ui.widgets.MainPage.GameTitle;
import ui.widgets.MainPage.NewAndLoadButton;
import ui.widgets.UnoFrame;

public final class MainPage extends UnoFrame {
    NewAndLoadButton newAndLoadButton;
    public void createAndAdjustMainContainer(Container rightContainer, Container centerContainer, Container leftContainer) {
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
        mainConstraints.fill = GridBagConstraints.NONE;
        mainConstraints.weightx = 1.0;
        mainConstraints.weighty = 1.0;
        this.addWidget(mainContainer, mainConstraints);
    }

    public void createAndAdjustTitle(Container conatiner) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        GameTitle g = new GameTitle(new FlowLayout(FlowLayout.CENTER, 0, 0));
        g.setMargin(0, 0, 75, 0);
        g.setOpaque(false);
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        constraints.weighty = 0;
        conatiner.addWidget(g, constraints);
    }

    public void createAndAdjustListOfButtons(Container conatiner) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        newAndLoadButton = new NewAndLoadButton(this);
        newAndLoadButton.setMargin(0, 0, 100, 0);
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        constraints.weighty = 0;
        newAndLoadButton.setOpaque(false);
        conatiner.addWidget(newAndLoadButton, constraints);
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
        super();
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

        createAndAdjustMainContainer(rightContainer, centerContainer, leftContainer);
        SwingUtilities.invokeLater(() -> {
            this.revalidate();
            this.repaint();
        });
    }

    public static void main(String[] args) {
        @SuppressWarnings("unused")
        MainPage main = new MainPage();
    }

    public NewAndLoadButton getNewAndLoadButton() {
        return newAndLoadButton;
    }
}
