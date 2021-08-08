package Game.UI;

// Observer Pattern for changing Screens.
// Subject of said pattern, attaches observer + notifies when it is required to change screens.
public interface ChangePanelSubject {
    void setChangePanelObserver(ChangePanelObserver changePanelObserver);

    void notify(String s);
}
