package Game.Enemies;

import Game.Map.Position;

public class Demon extends Enemy {
    public Demon() {
        super("Demon",
                10000,
                new Weapon("Demon's Weapon", new Dice(1, 6, 1000)),
                10000,
                "Just-Run.",
                "demon.png");
    }

    @Override
    public Position getDrawablePosition() {
        return new Position(position.getI(), position.getJ() - 1);
    }
}
