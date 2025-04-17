package ui.widgets.MainPage;

import components.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import util.*;

public class NewAndLoadButton extends JComponent {

    public NewAndLoadButton(LayoutManager mgr) {
        super();
        setLayout(mgr);
        Button newGameButton = new Button("New game", new Dimension(300, 50));
        newGameButton.setBGColor(new Color(0x93FF75));
        newGameButton.adjustFont(Util.loadCustomFont("src\\fonts\\Main font\\Roboto-Bold.ttf", 20));
        GridBagConstraints newGameButtonConst = new GridBagConstraints();
        newGameButtonConst.gridx = 0;
        newGameButtonConst.gridy = 0;
        newGameButtonConst.insets = new Insets(10, 0, 10, 0);
        Button loadGameButton = new Button("Load game", new Dimension(300, 50));
        loadGameButton.setBGColor(new Color(0xCACACA));
        loadGameButton.adjustFont(Util.loadCustomFont("src\\fonts\\Main font\\Roboto-Bold.ttf", 20));
        GridBagConstraints loadGameButtonConst = new GridBagConstraints();
        loadGameButtonConst.gridx = 0;
        loadGameButtonConst.gridy = 1;

        newGameButton.onHoverBrighten(-0.1f);
        loadGameButton.onHoverBrighten(-0.1f);
        
        add(newGameButton, newGameButtonConst);
        add(loadGameButton, loadGameButtonConst);
    }

    public NewAndLoadButton() {
        this(new GridBagLayout());
    }

    public void setMargin(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }


}
