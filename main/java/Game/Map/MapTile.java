package Game.Map;

import Game.Drawable;
import Game.Entity;

import java.awt.*;
import java.util.ArrayList;

public class MapTile implements Drawable {

    private MapTileType mapTileType;
    private MapTileState mapTileState;
    private Position position;
    private ArrayList<String> sprites;

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

    @Override
    public Position getDrawablePosition() {
        return position;
    }
}
