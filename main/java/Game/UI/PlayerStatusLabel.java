package Game.UI;

import javax.swing.*;
import java.awt.*;

public class PlayerStatusLabel extends JLabel implements PlayerStatusTranscriptReader {
    public PlayerStatusLabel() {
        super();
        setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    @Override
    public void readPlayerStatusTranscript(String s) {
        super.setText(s);
    }
}
