package components;


import javax.swing.JOptionPane;

public class MessageDialog extends JOptionPane{
    public MessageDialog(){
        super();
    }   
    public void show(String content){
        showMessageDialog(this,content);
    }
}
