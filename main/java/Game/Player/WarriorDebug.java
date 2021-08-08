package Game.Player;

public class WarriorDebug extends Warrior {
    public WarriorDebug(String name) {
        super(name);
    }

    @Override
    public int dealDamage() {
        return 10001;
    }
}
