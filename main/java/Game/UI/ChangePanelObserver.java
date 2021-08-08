package Game.UI;

// Observer Pattern for changing Screens.
// Observer of said pattern, reads updates from subjects.
public interface ChangePanelObserver {
    void update(String s);
}
