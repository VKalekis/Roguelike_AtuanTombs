package Game.Items;

import java.util.ArrayList;

public interface Item {
    String getName();
    String getDescription();
    String getSprite();
    ArrayList<ItemEffect> getItemEffects();
}