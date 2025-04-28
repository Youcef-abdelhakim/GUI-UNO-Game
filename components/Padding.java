package components;

import java.awt.Dimension;
import javax.swing.Box;

public class Padding extends Container {
    public Padding(int height,int width){
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        this.setOpaque(false);
    }
}
