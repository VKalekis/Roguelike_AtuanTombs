package Game.Items;

import Game.Drawable;
import Game.Map.Position;

import java.util.ArrayList;

public interface Item extends Drawable {
    String getName();
    //String getDescription();
    Position getPosition();
    ArrayList<ItemEffect> getItemEffects();
}