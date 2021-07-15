package Game.Items;

public interface Usable extends Item {
    int getUsesLeft();

    void decreaseUses();
}
