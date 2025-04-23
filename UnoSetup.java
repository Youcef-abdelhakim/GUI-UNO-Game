import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UnoSetup extends JFrame {
    private JTextField playersField;
    private JTextField humansField;
    private int numberOfPlayers;
    private int numberOfHumans;

    public UnoSetup() {
        setTitle("UNO!");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // layout personnalisé
        getContentPane().setBackground(new Color(14, 62, 110));

        // Titre
        JLabel titleLabel = new JLabel("UNO!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBounds(140, 10, 120, 30);
        add(titleLabel);

        // Champ pour nombre de joueurs
        JLabel playersLabel = new JLabel("How many players (2-4):");
        playersLabel.setBounds(100, 60, 200, 25);
        playersLabel.setForeground(Color.WHITE);
        add(playersLabel);

        playersField = new JTextField();
        playersField.setBounds(100, 85, 200, 25);
        add(playersField);

        // Champ pour nombre d'humains
        JLabel humansLabel = new JLabel("How many humans:");
        humansLabel.setBounds(100, 120, 200, 25);
        humansLabel.setForeground(Color.WHITE);
        add(humansLabel);

        humansField = new JTextField();
        humansField.setBounds(100, 145, 200, 25);
        add(humansField);

        // Bouton Next
        JButton nextButton = new JButton("Next");
        nextButton.setBounds(150, 190, 100, 30);
        add(nextButton);
        nextButton.setBackground(Color.GREEN);

        nextButton.addActionListener((ActionEvent e) -> {
            new UnoStartScreen(numberOfHumans);
            dispose();

        });


        // Bouton Return
        JButton returnButton = new JButton("↩");
        returnButton.setBounds(10, 220, 60, 30);
        add(returnButton);
        returnButton.setBackground(Color.GREEN);

        // Action bouton Next
        nextButton.addActionListener(e -> {
            try {
                numberOfPlayers = Integer.parseInt(playersField.getText());
                numberOfHumans = Integer.parseInt(humansField.getText());

                if (numberOfPlayers < 2 || numberOfPlayers > 4) {
                    JOptionPane.showMessageDialog(this, "Enter a number of players between 2 and 4.");
                } else if (numberOfHumans < 0 || numberOfHumans > numberOfPlayers) {
                    JOptionPane.showMessageDialog(this, "Invalid number of humans.");
                } else {
                    // Les valeurs sont valides, on peut les utiliser
                    System.out.println("Players: " + numberOfPlayers);
                    System.out.println("Humans: " + numberOfHumans);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
            }
        });

        // Action bouton Return
        returnButton.addActionListener(e -> {
            // Ici tu peux faire un retour au menu précédent
            JOptionPane.showMessageDialog(this, "Return pressed.");
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {

        new UnoSetup();

    }
}