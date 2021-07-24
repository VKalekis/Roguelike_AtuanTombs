package Game.Map;

import Game.Entity;

import java.awt.*;
import java.util.ArrayList;

public class MapTile {

    private MapTileType mapTileType;
    private MapTileState mapTileState;
    private Position position;
    private ArrayList<String> sprites;
    private int TILE_WIDTH = 11;
    private int TILE_HEIGHT = 11;
    private int color;
    private int cost;
    private Entity occupiedEntity;

    public MapTile(MapTileType mapTileType, Position position) {
        this.mapTileType = mapTileType;
        this.mapTileState = MapTileState.UNKNOWN;
        this.position = position;
        this.sprites = new ArrayList<>();
        this.color = 0;
        this.cost = 0;
        this.occupiedEntity = null;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void drawMapTile(Graphics g) {

//        Graphics2D g2d = (Graphics2D) g;
//
//        Image img1, img2;
//        try {
//            ClassLoader classLoader = getClass().getClassLoader();
//
//            img1 = ImageIO.read(new File(classLoader.getResource("wall.png").getFile()));
//            img2 = ImageIO.read(new File(classLoader.getResource("floor.png").getFile()));
//            g.setColor(new Color(100, 60, 40));
//            g.fillRect(position.getI() * TILE_WIDTH, position.getJ() * TILE_HEIGHT,
//                    TILE_WIDTH, TILE_HEIGHT);
//            switch (mapTileType) {
//                case WALL:
//
//                    g2d.drawImage(img1, position.getI() * TILE_WIDTH, position.getJ() * TILE_HEIGHT, null);
//                    break;
//                case FLOOR:
//
//                    g2d.drawImage(img2, position.getI() * TILE_WIDTH, position.getJ() * TILE_HEIGHT, null);
//                    break;
//            }
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }

        g.setColor(new Color(100, 60, 40));
        g.fillRect(position.getI() * TILE_WIDTH, position.getJ() * TILE_HEIGHT,
                TILE_WIDTH, TILE_HEIGHT);
        switch (mapTileType) {
            case WALL:

                g.setColor(new Color(180, 100, 100));
                g.fillRect(position.getI() * TILE_WIDTH, position.getJ() * TILE_HEIGHT,
                        TILE_WIDTH - 1, TILE_HEIGHT - 1);
                break;
            case FLOOR:

                g.setColor(new Color(130, 130, 130));
                g.fillRect(position.getI() * TILE_WIDTH, position.getJ() * TILE_HEIGHT,
                        TILE_WIDTH - 1, TILE_HEIGHT - 1);
                break;
        }
    }

    public MapTileType getMapTileType() {
        return mapTileType;
    }

    public void setMapTileType(MapTileType mapTileType) {
        this.mapTileType = mapTileType;
    }

    public Position getPosition() {
        return position;
    }

    public MapTileState getMapTileState() {
        return mapTileState;
    }

    public void setMapTileState(MapTileState mapTileState) {
        this.mapTileState = mapTileState;
    }

    @Override
    public String toString() {
        return "MapTile{" +
                "mapTileType=" + mapTileType +
                ", mapTileState=" + mapTileState +
                ", position=" + position +
                ", TILE_WIDTH=" + TILE_WIDTH +
                ", TILE_HEIGHT=" + TILE_HEIGHT +
                ", color=" + color +
                '}';
    }

    public ArrayList<String> getSprites() {
        return sprites;
    }

    public void addsprite(String sprite) {
        this.sprites.add(sprite);
    }

    public void deleteTexture() {
        this.sprites = new ArrayList<>();
    }

    public double getDistance(MapTile mapTile1){
        return this.position.getDistance(mapTile1.getPosition());
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isOccupied() {
        return occupiedEntity!=null;
    }

    public void setOccupiedEntity(Entity entity) {
        this.occupiedEntity = entity;
    }

    public Entity getOccupiedEntity() {
        return occupiedEntity;
    }

    public void setEmpty() {
        this.occupiedEntity=null;
    }
}
