package Game.UI;

// Observer Pattern for transferring messages/strings to be displayed at the GameLog.
// Subject of said pattern, attaches observer + sends transcript when a change occurs.
public interface GameLogTranscriptWriter {
    void setGameLogTranscriptReader(GameLogTranscriptReader gameLogTranscriptReader);

    void sendGameLogTranscript(String s);
}
