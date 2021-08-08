package Game.Player;

public class WizardDebug extends Wizard {
    public WizardDebug(String name) {
        super(name);
    }

    @Override
    public int dealDamage() {
        return 10001;
    }
}
