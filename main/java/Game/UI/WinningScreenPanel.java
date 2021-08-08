package Game.UI;

import javax.swing.*;
import java.awt.*;

public class WinningScreenPanel extends JPanel {
    public WinningScreenPanel() {
        JLabel jLabel = new JLabel("<html><h1>\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89</h1>\n" +
                "<h2>Woohoo! You reached the exit of the Tombs! <br /></h2></html>", SwingConstants.CENTER);
        this.setLayout(new BorderLayout());
        this.add(jLabel, BorderLayout.CENTER);
    }
}
