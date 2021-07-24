package Game.Items;

public interface Usable extends Item {
    int getUsesLeft();
    int getUses();
    void decreaseUses();
}
