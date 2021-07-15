package Game.Map;

import Game.PlayerSrc.AbstractPlayer;

public class Camera {


    private Position position;
    private int screenSize;
    private int mapSize;

    public Camera(int screenSize, int mapSize) {
        this.position = new Position();
        this.screenSize = screenSize;
        this.mapSize = mapSize;
    }

    public int getScreenSize() {
        return screenSize;
    }

    public void setNewPosition(AbstractPlayer player) {
        this.position.moveTo(internalSetNewPosition(player.getPosition().getI()),
                internalSetNewPosition(player.getPosition().getJ()));
    }

    //http://www.roguebasin.com/index.php/Scrolling_map
    private int internalSetNewPosition(int playerIorJ) {


        //For 31x31 camera centered around player:
        //
        //At the left border, the marginal state is:
        // 0 - 15 - 30
        //LeftCameraBorder - Player - RightCameraBorder
        //The camera will be fixed at 0 if player.position <= 15 = (screenSize+1)/2
        if (playerIorJ < (screenSize + 1) / 2) {
            return 0;
        }
        //At the right border, the marginal state is:
        //31 - 46 - 61
        //The camera will be fixed at 31 if player.position >= 46 = 62 - 32/2 = mapSize - (screenSize+1)/2
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
