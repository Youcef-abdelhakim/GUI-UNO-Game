package ui.widgets.MainPage;

import components.Text;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import util.Util;

public class GameTitle extends JComponent {

    public GameTitle(LayoutManager mgr) {
        super();
        setLayout(mgr);
        Text gameTitle = new Text("UNO!!!", Util.loadCustomFont("src\\fonts\\Main font\\Roboto-Black.ttf", 50), Color.white);
        GridBagConstraints titleConst = new GridBagConstraints();
        titleConst.gridx = 0;
        titleConst.gridy = 0;
        add(gameTitle, titleConst);
        setOpaque(false);
    }

    public void setMargin(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }

}
