package Game;

import Game.Map.Position;

public interface Entity {
    int dealDamage();
    void takeDamage(int dmg);
    Position getPosition();
}
