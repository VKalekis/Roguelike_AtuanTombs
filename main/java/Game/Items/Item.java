package Game.Items;

import Game.Map.Position;
import Game.State.Drawable;

import java.util.ArrayList;

public interface Item extends Drawable {
    String getName();

    //String getDescription();
    Position getPosition();

    ArrayList<ItemEffect> getItemEffects();
}