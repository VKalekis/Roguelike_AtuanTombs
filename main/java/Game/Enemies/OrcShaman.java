package Game.Enemies;

import Game.Map.Position;

public class OrcShaman extends Enemy{
    public OrcShaman() {
        super("Orc Shaman",
                50,
                new Weapon("Orc Shaman's Weapon", new Dice(3, 6, 4)),
                120,
                "You see someone dancing around a fire pit.",
                "orc_shaman.png");
    }
}
