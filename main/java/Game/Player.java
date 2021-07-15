package Game;

import Game.Map.Position;

public class Player {
    private Position position;

    public Player(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void movePlayer(String dir){
        position.shiftDir(dir);
    }

    public void setStartingPosition(Position position) {
        this.position = position;
    }
}
