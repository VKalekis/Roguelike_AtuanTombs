package Game.Enemies;

public class IceZombie extends Enemy {
    public IceZombie() {
        super("Ice Zombie",
                25,
                new Weapon("Ice Zombies's Weapon", new Dice(2, 6, 2)),
                30,
                "In the distance, you faintly hear ice cracking.",
                "ice_zombie.png");
    }
}
