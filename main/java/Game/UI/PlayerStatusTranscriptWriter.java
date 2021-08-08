package Game.UI;

// Observer Pattern for transferring the playerStats to the JLabel which displays them.
// Subject of said pattern, attaches observer + sends the playerStats.
public interface PlayerStatusTranscriptWriter {
    void setPlayerStatusTranscriptReader(PlayerStatusTranscriptReader playerStatusTranscriptReader);

    void sendPlayerStatusTranscript(String s);
}
