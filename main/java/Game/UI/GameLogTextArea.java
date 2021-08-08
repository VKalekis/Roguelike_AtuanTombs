package Game.UI;

import javax.swing.*;

public class GameLogTextArea extends JScrollPane implements GameLogTranscriptReader {
    private static JTextArea jTextArea = new JTextArea(8, 30);

    public GameLogTextArea() {
        super(jTextArea);
        jTextArea.setEditable(false);
    }

    @Override
    public void readGameLogTranscript(String s) {
        jTextArea.append(s);
    }
}
