package Game.UI;

import javax.swing.*;
import java.awt.*;

public class LosingScreenPanel extends JPanel {
    public LosingScreenPanel() {
        JLabel jLabel = new JLabel("<html><h2>Player HP has reached 0.<br />Game over!</h2>\n" +
                "<h3>Press X to close program and relaunch to try again!</h3></html>", SwingConstants.CENTER);
        this.setLayout(new BorderLayout());
        this.add(jLabel, BorderLayout.CENTER);
    }
}
