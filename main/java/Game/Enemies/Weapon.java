package Game.Enemies;

public class Weapon {

    private final String name;
    private final String description;
    private final Dice damageDice;

    public Weapon(String name, String description, Dice damageDice) {
        this.name = name;
        this.description = description;
        this.damageDice = damageDice;
    }

    public int hit() {
        return damageDice.roll();
    }

    @Override
    public String toString() {
        return "{ description= " + description + ", damageDice= " + damageDice;
    }
}