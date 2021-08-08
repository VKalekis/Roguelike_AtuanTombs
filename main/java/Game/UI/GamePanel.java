package Game.UI;

import Game.Map.MapComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GamePanel extends JPanel {

    private final MapComponent mapComponent;

    public GamePanel(ChangePanelObserver changePanelObserver) {

        this.setFocusable(true);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                GamePanel.this.requestFocusInWindow();
            }

        });

        PlayerStatusLabel jLabel = new PlayerStatusLabel();
        GameLogTextArea gameLogTextArea = new GameLogTextArea();
        this.mapComponent = new MapComponent(jLabel, gameLogTextArea, changePanelObserver);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel secondaryPanel = new JPanel();
        secondaryPanel.setLayout(new GridLayout(2, 1));

        mainPanel.add(mapComponent, BorderLayout.WEST);

        secondaryPanel.add(jLabel);
        secondaryPanel.add(gameLogTextArea);
        mainPanel.add(secondaryPanel, BorderLayout.EAST);

        add(mainPanel);
    }

    public void setInitialPlayerVariables(String playerClass, String playerName, boolean debug) {
        mapComponent.setInitialPlayerVariables(playerClass, playerName, debug);
    }
}
