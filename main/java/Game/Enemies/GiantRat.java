package Game.Enemies;

public class GiantRat extends Enemy{
    public GiantRat() {
        super("Giant Rat",
                10,
                new Weapon("GiantRat's Weapon", "Claw",
                        new Dice(1,6,1)),
                30,
                "Squeak squeak",
                "enemy.png");
    }
}
