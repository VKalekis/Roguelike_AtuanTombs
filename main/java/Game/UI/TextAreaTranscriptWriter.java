package Game.UI;

import Game.UI.TextAreaTranscriptReader;

public interface TextAreaTranscriptWriter {
    void setTextAreaTranscriptReader(TextAreaTranscriptReader textAreaTranscriptReader);
    void sendTextAreaTranscript(String s);
}
