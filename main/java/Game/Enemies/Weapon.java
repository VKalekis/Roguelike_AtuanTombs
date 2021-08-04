package Game.Enemies;

public class Weapon {

    private final String name;
    private final Dice damageDice;

    public Weapon(String name, Dice damageDice) {
        this.name = name;
        this.damageDice = damageDice;
    }

    public int hit() {
        return damageDice.roll();
    }

    @Override
    public String toString() {
        return "{ name= " + name + ", damageDice= " + damageDice;
    }
}