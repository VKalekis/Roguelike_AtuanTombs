package Game.UI;

// Observer Pattern for transferring the playerStats to the JLabel which displays them.
// Observer of said pattern, reads from subjects.
public interface PlayerStatusTranscriptReader {
    void readPlayerStatusTranscript(String s);
}
