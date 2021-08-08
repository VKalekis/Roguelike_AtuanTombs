package Game.Enemies;

public class Swampy extends Enemy {
    public Swampy() {
        super("Swampy",
                30,
                new Weapon("Swampy's Weapon", new Dice(2, 6, 4)),
                80,
                "You feel something slimy under your shoes.",
                "swampy.png");
    }
}
