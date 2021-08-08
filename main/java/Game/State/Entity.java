package Game.State;

import Game.Map.Position;

/*
An Entity (Player or Enemy) can:
    - Deal and receive damage
    - Be at a certain position. This position will be used to set the maptile an Entity stands on as occupied. When
        the Entity moves to a different position, the previous maptile will be set to empty.
    - Move to a different position.
 */
public interface Entity {
    int dealDamage();

    void takeDamage(int dmg);

    Position getPosition();

    void move(Position position);
}
