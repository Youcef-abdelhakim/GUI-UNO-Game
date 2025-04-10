package widgets;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.JFrame;


public class Frame extends JFrame{

    public Frame(String title,Color bg,Dimension d,LayoutManager layoutManager){
        super(title);
        this.setSize(d);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(bg);
        this.setLayout(layoutManager);
        this.setVisible(true);
    }   
    public Frame(String title,Color bg,Dimension d){
        super(title);
        this.setSize(d);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(bg);
        this.setVisible(true);
    }   
    public void addWidget(JComponent widget){
        this.add(widget);
    }
    public void addWidget(JComponent widget,String constraints){
        this.add(widget,constraints);
    }


}
