package Game.UI;

import javax.swing.*;
import java.awt.*;

public class FullGame implements ChangePanelObserver {

    private JPanel cards;
    private StartingScreenPanel startingScreenPanel;
    private GamePanel gamePanel;
    private WinningScreenPanel winningScreenPanel;
    private LosingScreenPanel losingScreenPanel;

    public void addComponentToPane(Container pane) {

        startingScreenPanel = new StartingScreenPanel(this);
        startingScreenPanel.addAllComponents();

        gamePanel = new GamePanel(this);

        winningScreenPanel = new WinningScreenPanel();

        losingScreenPanel = new LosingScreenPanel();

        // JPanel containing cards.
        cards = new JPanel(new CardLayout());
        cards.add(startingScreenPanel, "MENU");
        cards.add(gamePanel, "GAME");
        cards.add(winningScreenPanel, "WIN");
        cards.add(losingScreenPanel, "LOSE");

        pane.add(cards, BorderLayout.CENTER);
    }

    public void changeCards(String cardName) {
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, cardName);
    }

    public static void main(String[] args) throws Exception {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Atuan Tombs");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            FullGame demo = new FullGame();
            demo.addComponentToPane(frame.getContentPane());

            frame.pack();
            frame.setVisible(true);
        });

    }

    @Override
    public void update(String s) {
        System.out.println("Updated Listener " + s);
        changeCards(s);
        if ("GAME".equals(s)) {
            String[] menuValues = startingScreenPanel.getValues();
            gamePanel.setInitialPlayerVariables(menuValues[0].toLowerCase(), menuValues[1], menuValues[2].equalsIgnoreCase("debug"));
        }
    }
}


