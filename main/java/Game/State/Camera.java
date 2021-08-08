package Game.State;

import Game.Map.Position;

public class Camera {

    private final Position position;
    private final int screenSize;
    private final int mapSize;

    public Camera(int screenSize, int mapSize) {
        this.position = new Position();
        this.screenSize = screenSize;
        this.mapSize = mapSize;
    }

    public int getScreenSize() {
        return screenSize;
    }

    public void setNewPosition(Position position) {
        this.position.moveTo(internalSetNewPosition(position.getI()),
                internalSetNewPosition(position.getJ()));
    }

    //http://www.roguebasin.com/index.php/Scrolling_map
    private int internalSetNewPosition(int playerIorJ) {
        /* For 41x41 camera centered around player:

        At the left border, the marginal state is:
          0 - 20 - 40
        LeftCameraBorder - Player - RightCameraBorder
        The camera will be fixed at 0 if player.position < 21 = (screenSize+1)/2
         */
        if (playerIorJ < (screenSize + 1) / 2) {
            return 0;
        }
        /* At the right border, the marginal state is:
        21 - 41 - 61
        The camera will be fixed at 21 if player.position >= 41 = 62 - 42/2 = mapSize - (screenSize+1)/2
        */
        else if (playerIorJ >= mapSize - (screenSize + 1) / 2) {
            return mapSize - screenSize;
        }
        //Otherwise, the camera will be centered around player.
        else {
            return playerIorJ - (screenSize / 2);
        }
    }

    public Position getPosition() {
        return position;
    }
}
