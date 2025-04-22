package ui.views;

import components.Button;
import components.GameFrame;
import components.Label;
import components.Panel;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import ui.widgets.GamePage.PlayerListPanel;

public class GamePage {
    private GameFrame gameFrame;
    private Label lastCardPlayed;
    private Label lastCardLabel;
    private Label currentPlayerLabel;
    private Button unoBtn;
    private Panel playerHandPanel;
    private PlayerListPanel playerListPanel;
    private String[] players = {"player1", "player2", "player3", "player4"};
    private int currentPlayerIndex = 0;
    private ArrayList<String> playerHand = new ArrayList<>();

    private ArrayList<ImageIcon> cardIcons = new ArrayList<>();
    private HashMap<String, ImageIcon> cardImageMap = new HashMap<>();

    public GamePage() {
        // Initialize frame with null layout for absolute positioning
        gameFrame = new GameFrame("UNO Game", new Color(0x042e54), new Dimension(900, 700), null);
        

        // Load all card images
        loadAllCardImages();

        // 1. Player list (top left corner)
        playerListPanel = new PlayerListPanel(players);
        playerListPanel.setBounds(30, 30, 200, 200);
        gameFrame.addWidget(playerListPanel);

        // 2. "The last card played" text (full width top)
        lastCardLabel = new Label("LAST CARD PLAYED", Label.CENTER);
        lastCardLabel.setFont(new Font("Arial", Font.BOLD, 30));
        lastCardLabel.setTextColor(Color.WHITE);
        lastCardLabel.setBounds(0, 10, 900, 50);
        gameFrame.addWidget(lastCardLabel);

        // 3. Last played card (center of screen)
        lastCardPlayed = new Label();
        lastCardPlayed.setBounds(375, 110, 150, 230);
        setCardImage(lastCardPlayed, "Nine_Blue.png");
        gameFrame.addWidget(lastCardPlayed);

        // 4. Player's hand cards with scrolling
        playerHandPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        JScrollPane handScrollPane = playerHandPanel.makeScrollable(780, 140);
        handScrollPane.setBounds(50, 400, 800, 185);
        gameFrame.addWidget(handScrollPane);

        // 5. Player's turn indicator (bottom left)
        currentPlayerLabel = new Label("PLAYER 1'S TURN", Label.LEFT);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        currentPlayerLabel.setTextColor(Color.WHITE);
        currentPlayerLabel.setBounds(50, 620, 300, 30);
        gameFrame.addWidget(currentPlayerLabel);

        // 6. UNO button (bottom right)
        unoBtn = new Button("UNO");
        unoBtn.setBounds(700, 610, 150, 50);
        unoBtn.setBGColor(Color.GRAY);
        unoBtn.setForeground(Color.BLACK);
        unoBtn.adjustFont(new Font("Arial", Font.BOLD, 24));
        unoBtn.setEnabled(false);
        unoBtn.addActionListener(e -> JOptionPane.showMessageDialog(gameFrame, "UNO called!"));
        gameFrame.addWidget(unoBtn);

        // Example hand setup
        playerHand.add("Nine_Red.png");
        playerHand.add("One_Blue.png");
        playerHand.add("Six_Red.png");
        playerHand.add("Eigth_Yellow.png");
        playerHand.add("Nine_Green.png");
        playerHand.add("Nine_Red.png");
        playerHand.add("One_Blue.png");
        playerHand.add("Six_Red.png");
        playerHand.add("Eigth_Yellow.png");
        playerHand.add("Nine_Green.png");
        
        // Show initial cards
        showPlayerHand();

        // Refresh frame
        gameFrame.revalidate();
        gameFrame.repaint();
    }

    private void showPlayerHand() {
        playerHandPanel.removeAll();
        
        // Fixed card dimensions
        int cardWidth = 100; // Fixed width for all cards
        int cardHeight = 150; // Fixed height
        
        for (String cardName : playerHand) {
            Button card = new Button();
            ImageIcon icon = cardImageMap.get(cardName);
            if (icon != null) {
                Image scaled = icon.getImage().getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
                card.setIcon(new ImageIcon(scaled));
                card.setPreferredSize(new Dimension(cardWidth, cardHeight));
                card.setMaximumSize(new Dimension(cardWidth, cardHeight)); // Prevent stretching
                card.setBorder(null);
                card.setContentAreaFilled(false);
                card.addActionListener(e -> playCard(cardName));
                playerHandPanel.add(card);
            }
        }
        
        // Calculate total width needed
        FlowLayout layout = (FlowLayout) playerHandPanel.getLayout();
        int hgap = layout.getHgap();
        int totalWidth = (cardWidth + hgap) * playerHand.size();
        playerHandPanel.setPreferredSize(new Dimension(totalWidth, cardHeight));
        
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    private void loadAllCardImages() {
        // List of all possible UNO card names
        String[] colors = {"Red", "Blue", "Green", "Yellow"};
        String[] numbers = {"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eigth", "Nine"};
        String[] actions = {"Skip", "Reverse", "Plus_Two"};
        String[] wilds = {"Wild", "WildFour"};
        
        try {
            // Load colored number cards
            for (String number : numbers) {
                for (String color : colors) {
                    loadCardImage(number + "_" + color + ".png");
                }
            }
            
            // Load colored action cards
            for (String action : actions) {
                for (String color : colors) {
                    loadCardImage(action + "_" + color + ".png");
                }
            }
            
            // Load wild cards
            for (String wild : wilds) {
                loadCardImage(wild + ".png");
            }
            
            System.out.println("Loaded " + cardIcons.size() + " card images");
        } catch (Exception e) {
            System.err.println("Error loading card images");
            e.printStackTrace();
        }
    }
    
    private void loadCardImage(String filename) {
        try {
            URL imageUrl = getClass().getResource("/cardimages/" + filename);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                cardIcons.add(icon);
                cardImageMap.put(filename, icon);
            } else {
                System.err.println("Card image not found: " + filename);
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + filename);
            e.printStackTrace();
        }
    }

    private void setCardImage(Label label, String filename) {
        ImageIcon icon = cardImageMap.get(filename);
        if (icon != null) {
            Image scaled = icon.getImage().getScaledInstance(150, 230, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaled));
        } else {
            label.setText("Card Missing");
            System.err.println("Card image not loaded: " + filename);
        }
    }

    private void showCards() {
        playerHandPanel.removeAll();
        for (String cardName : playerHand) {
            Button card = new Button();
            ImageIcon icon = cardImageMap.get(cardName);
            if (icon != null) {
                Image scaled = icon.getImage().getScaledInstance(80, 120, Image.SCALE_SMOOTH);
                card.setIcon(new ImageIcon(scaled));
                card.setBorder(null);
                card.setContentAreaFilled(false);
                card.addActionListener(e -> playCard(cardName));
                playerHandPanel.add(card);
            }
        }
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    private void playCard(String cardName) {
        playerHand.remove(cardName);
        setCardImage(lastCardPlayed, cardName);
        showPlayerHand();

        // UNO check
        unoBtn.setBGColor(playerHand.size() == 1 ? Color.GREEN : Color.GRAY);
        unoBtn.setEnabled(playerHand.size() == 1);

        // Move to next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        currentPlayerLabel.setText(players[currentPlayerIndex] + "'s turn");
        playerListPanel.updateCurrentPlayer(currentPlayerIndex);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GamePage());
    }
}