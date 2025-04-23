import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UnoStartScreen extends JFrame {

    public UnoStartScreen(int num) {
        setTitle("UNO!");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre

        // Panel principal
        JPanel panel = new JPanel();
        panel.setBackground(new Color(14, 62, 110)); // Couleur de fond bleu foncé
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));





        // Titre
        JLabel title = new JLabel("UNO!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Champs texte
        JTextField[] playerFields = new JTextField[4];


        switch (num){
            case 2:

                for (int i = 0; i < 2; i++) {
                    JLabel label = new JLabel("Name player " + (i + 1) + ":");
                    label.setForeground(Color.WHITE);
                    panel.add(label);
                    playerFields[i] = new JTextField(20);
                    playerFields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                    panel.add(playerFields[i]);
                    panel.add(Box.createRigidArea(new Dimension(0, 10)));
                }

                break;
            case 3:

                for (int i = 0; i < 3; i++) {
                    JLabel label = new JLabel("Name player " + (i + 1) + ":");
                    label.setForeground(Color.WHITE);
                    panel.add(label);
                    playerFields[i] = new JTextField(20);
                    playerFields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                    panel.add(playerFields[i]);
                    panel.add(Box.createRigidArea(new Dimension(0, 10)));
                }

                break;

            case 4:

                for (int i = 0; i < 4; i++) {
                    JLabel label = new JLabel("Name player " + (i + 1) + ":");
                    label.setForeground(Color.WHITE);
                    panel.add(label);
                    playerFields[i] = new JTextField(20);
                    playerFields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                    panel.add(playerFields[i]);
                    panel.add(Box.createRigidArea(new Dimension(0, 10)));
                }

                break;
        }


        //return botton
        JButton aReturn = new JButton("return");
        JButton returnButton = new JButton("↩");
        aReturn.setBounds(10, 220, 60, 30);
        add(aReturn);
        aReturn.setBackground(Color.GREEN);
        panel.add(aReturn);

        aReturn.addActionListener((ActionEvent e) -> {
            new UnoSetup();
            dispose();

        });


        // Bouton Play
        JButton playButton = new JButton("PLAY!!");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setForeground(Color.BLACK);
        playButton.setBackground(Color.GREEN);
        playButton.setFocusPainted(false);
        playButton.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(playButton);

        // Action du bouton
        playButton.addActionListener((ActionEvent e) -> {
            StringBuilder names = new StringBuilder("Players: ");
            for (JTextField field : playerFields) {
                names.append(field.getText()).append(" ");
            }
            JOptionPane.showMessageDialog(this, names.toString());
        });


        add(panel);
        setVisible(true);
    }


}