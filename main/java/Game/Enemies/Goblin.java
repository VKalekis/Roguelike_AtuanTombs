package Game.Enemies;

import Game.Map.Position;

public class Goblin extends Enemy {
    public Goblin() {
        super("Goblin",
                15,
                new Weapon("GiantRat's Weapon", new Dice(1, 6, 1)),
                30,
                "It's your friendly neighborhood goblin...man!",
                "goblin.png");
    }
}
