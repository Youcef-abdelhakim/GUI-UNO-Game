package ui.widgets.MainPage;

import components.Text;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import util.Util;

public class GameTitle extends JComponent {

    public GameTitle(LayoutManager mgr) {
        super();
        setLayout(mgr);
        Text gameTitle = new Text("UNO!!!", Util.loadCustomFont("src\\fonts\\Main font\\Roboto-Black.ttf", 50), Color.white);

        if (mgr instanceof GridBagLayout) {
            GridBagConstraints titleConst = new GridBagConstraints();
            titleConst.gridx = 0;
            titleConst.gridy = 0;
            titleConst.anchor = GridBagConstraints.CENTER;
            titleConst.fill = GridBagConstraints.CENTER;
            add(gameTitle, titleConst);
        }else{
            add(gameTitle);
        }
        setOpaque(false);
    }
    
    public GameTitle() {
        super();
        Text gameTitle = new Text("UNO!!!", Util.loadCustomFont("src\\fonts\\Main font\\Roboto-Black.ttf", 50), Color.white);
        setOpaque(false);
    }

    public void setMargin(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }

}
