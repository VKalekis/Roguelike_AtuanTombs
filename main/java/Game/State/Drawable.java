package Game.State;

import Game.Map.Position;

import java.util.List;

/*
Any component which is drawn on the map/screen must implement this interface.
The list of sprites will be drawn at the map position given by drawablePosition.

While setting the wall sprites, some floors get an extra sprite to accommodate the drawing of a neighbour wall
(which is not so pretty while playing the game but it is what it is). So, a list of sprites must be provided.

In some cases, if the sprite is larger than 16x16, a different drawable position has to be provided so that the
main component of the sprite is drawn at the correct maptile.
 */
public interface Drawable {
    List<String> getSprites();

    Position getDrawablePosition();
}
