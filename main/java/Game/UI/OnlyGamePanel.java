package Game.UI;

import javax.swing.*;
import java.awt.*;

public class OnlyGamePanel {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MyFrame frame = new MyFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class MyFrame extends JFrame {
    private ChangePanelObserver changePanelObserver;

    public MyFrame() throws HeadlessException {
        GamePanel gamePanel = new GamePanel(changePanelObserver);
        gamePanel.setInitialPlayerVariables("Wizard".toLowerCase(), "Merlin", false);
        add(gamePanel);
        pack();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1100, 700);
    }
}

