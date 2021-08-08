package Game.Items;

import Game.Player.SlotType;

public interface Equippable extends Item {
    SlotType getSlot();

    String toStringHTML();
}