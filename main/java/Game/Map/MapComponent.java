package Game.Map;

import Game.Player.SlotType;
import Game.State.GameState;
import Game.UI.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MapComponent extends JComponent implements PlayerStatusTranscriptWriter, GameLogTranscriptWriter {

    private final GameState gameState;
    private PlayerStatusTranscriptReader playerStatusTranscriptReader;
    private GameLogTranscriptReader gameLogTranscriptReader;

    public MapComponent(PlayerStatusTranscriptReader playerStatusTranscriptReader,
                        GameLogTranscriptReader gameLogTranscriptReader,
                        ChangePanelObserver changePanelObserver) {

        setPlayerStatusTranscriptReader(playerStatusTranscriptReader);
        setGameLogTranscriptReader(gameLogTranscriptReader);

        gameState = new GameState(playerStatusTranscriptReader, gameLogTranscriptReader, changePanelObserver);
        addKeyBinds();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameState.drawMap(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700, 700);
    }

    @Override
    public void setPlayerStatusTranscriptReader(PlayerStatusTranscriptReader r) {
        this.playerStatusTranscriptReader = r;
    }

    @Override
    public void sendPlayerStatusTranscript(String s) {
        this.playerStatusTranscriptReader.readPlayerStatusTranscript(s);
    }

    @Override
    public void setGameLogTranscriptReader(GameLogTranscriptReader r) {
        this.gameLogTranscriptReader = r;
    }

    @Override
    public void sendGameLogTranscript(String s) {
        this.gameLogTranscriptReader.readGameLogTranscript(s);
    }

    public void setInitialPlayerVariables(String playerClass, String playerName, boolean debug) {
        gameState.setInitialPlayerVariables(playerClass, playerName, debug);
    }


    private void addKeyBinds() {
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "movePlayerN");
        this.getActionMap().put("movePlayerN", new MovePlayerAction("N"));

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "movePlayerS");
        this.getActionMap().put("movePlayerS", new MovePlayerAction("S"));

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "movePlayerW");
        this.getActionMap().put("movePlayerW", new MovePlayerAction("W"));

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "movePlayerE");
        this.getActionMap().put("movePlayerE", new MovePlayerAction("E"));

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F"), "goToNextRoom");
        this.getActionMap().put("goToNextRoom", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameState.goToNextRoom();
                gameState.moveEnemies();
                gameState.spawnEnemies();
                sendPlayerStatusTranscript(gameState.getPlayerStatsHTML());
                repaint();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "playerDealDamage");
        this.getActionMap().put("playerDealDamage", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameState.playerdealDamage();
                gameState.moveEnemies();
                gameState.spawnEnemies();
                sendPlayerStatusTranscript(gameState.getPlayerStatsHTML());
                repaint();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("U"), "useUsable");
        this.getActionMap().put("useUsable", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameState.useUsable();
                gameState.moveEnemies();
                gameState.spawnEnemies();
                sendPlayerStatusTranscript(gameState.getPlayerStatsHTML());
                repaint();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("P"), "dropUsable");
        this.getActionMap().put("dropUsable", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameState.dropUsable();
                gameState.moveEnemies();
                gameState.spawnEnemies();
                sendPlayerStatusTranscript(gameState.getPlayerStatsHTML());
                repaint();
            }
        });

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, 0), "moveInventoryCursorL");
        this.getActionMap().put("moveInventoryCursorL", new MoveInventoryCursor(-1));

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, 0), "moveInventoryCursorR");
        this.getActionMap().put("moveInventoryCursorR", new MoveInventoryCursor(+1));

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("T"), "equipWeapon");
        this.getActionMap().put("equipWeapon", new EquipEquippable(SlotType.MAIN_HAND));

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("G"), "equipShield");
        this.getActionMap().put("equipShield", new EquipEquippable(SlotType.OFF_HAND));

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), "rest");
        this.getActionMap().put("rest", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameState.rest();
                gameState.moveEnemies();
                gameState.spawnEnemies();
                sendPlayerStatusTranscript(gameState.getPlayerStatsHTML());
                repaint();
            }
        });
    }

    private class MovePlayerAction extends AbstractAction {
        private final String dir;

        MovePlayerAction(String dir) {
            this.dir = dir;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gameState.movePlayer(this.dir);
            gameState.moveEnemies();
            gameState.spawnEnemies();
            sendPlayerStatusTranscript(gameState.getPlayerStatsHTML());
            repaint();
        }
    }

    private class MoveInventoryCursor extends AbstractAction {
        private final int offset;

        MoveInventoryCursor(int offset) {
            this.offset = offset;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gameState.moveInventoryCursor(this.offset);
            sendPlayerStatusTranscript(gameState.getPlayerStatsHTML());
            repaint();
        }
    }

    private class EquipEquippable extends AbstractAction {
        private final SlotType slotType;

        public EquipEquippable(SlotType slotType) {
            this.slotType = slotType;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gameState.equipEquippable(this.slotType);
            gameState.moveEnemies();
            gameState.spawnEnemies();
            sendPlayerStatusTranscript(gameState.getPlayerStatsHTML());
            repaint();
        }
    }


}
