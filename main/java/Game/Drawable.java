package Game;

import Game.Map.Position;

import java.util.List;

public interface Drawable {
    List<String> getSprites();
    Position getDrawablePosition();
}
