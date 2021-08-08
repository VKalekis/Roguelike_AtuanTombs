package Game.UI;

// Observer Pattern for transferring messages/strings to be displayed at the GameLog.
// Observer of said pattern, reads from subjects.
public interface GameLogTranscriptReader {
    void readGameLogTranscript(String s);
}
