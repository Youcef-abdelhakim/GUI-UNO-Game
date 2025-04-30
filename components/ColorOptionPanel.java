package components;

import java.awt.Component;
import javax.swing.JOptionPane;

public class ColorOptionPanel extends JOptionPane {
    
    // Message types
    public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    public static final int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;
    public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
    public static final int QUESTION_MESSAGE = JOptionPane.QUESTION_MESSAGE;
    public static final int PLAIN_MESSAGE = JOptionPane.PLAIN_MESSAGE;
    
    // Option types
    public static final int DEFAULT_OPTION = JOptionPane.DEFAULT_OPTION;
    public static final int YES_NO_OPTION = JOptionPane.YES_NO_OPTION;
    public static final int YES_NO_CANCEL_OPTION = JOptionPane.YES_NO_CANCEL_OPTION;
    public static final int OK_CANCEL_OPTION = JOptionPane.OK_CANCEL_OPTION;
    
    public static Object showColorSelectionDialog(Component parent, String title, String message, Object[] options, Object initialValue) {
        return showOptionDialog(
            parent,
            message,
            title,
            DEFAULT_OPTION,
            QUESTION_MESSAGE,
            null,
            options,
            initialValue
        );
    }
    
    public static void showMessageDialog(Component parent, String message, String title, int messageType) {
        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }
    
    public static int showConfirmDialog(Component parent, String message, String title, int optionType) {
        return JOptionPane.showConfirmDialog(parent, message, title, optionType, QUESTION_MESSAGE);
    }
    
    public static int showConfirmDialog(Component parent, String message, String title, int optionType, int messageType) {
        return JOptionPane.showConfirmDialog(parent, message, title, optionType, messageType);
    }
}