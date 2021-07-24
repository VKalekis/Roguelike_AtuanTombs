package Game.UI;

import Game.UI.LabelTranscriptReader;

public interface LabelTranscriptWriter {
    void setLabelTranscriptReader(LabelTranscriptReader labelTranscriptReader);
    void sendLabelTranscript(String s);
}
