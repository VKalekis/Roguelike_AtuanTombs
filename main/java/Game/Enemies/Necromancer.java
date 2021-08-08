package Game.Enemies;

public class Necromancer extends Enemy {
    public Necromancer() {
        super("Necromancer",
                60,
                new Weapon("Necromancer's Weapon", new Dice(4, 6, 4)),
                150,
                "You hear the sound of a distant chant.",
                "necromancer1.png");
    }
}
