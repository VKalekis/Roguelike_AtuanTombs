package Game.Enemies;

import Game.Astar;
import Game.Entity;
import Game.Map.MapTile;
import Game.Map.MapTileType;
import Game.Map.Position;
import Game.Map.RoomMap;

import java.util.*;

public class Enemy implements Entity {

    private final String name;
    private int hitpoints;
    private final Weapon weapon;
    private final int experience;
    private final String announcementText;
    private Position position;
    private final String textureName;
    private final Astar astar;
    private List<Position> nextMoves;

    public Enemy(String name, int hitpoints, Weapon weapon, int experience,
                 String announcementText, String textureName) {
        this.hitpoints = hitpoints;
        this.name = name;
        this.weapon = weapon;
        this.experience = experience;
        this.announcementText = announcementText;
        this.textureName = textureName;

        astar = new Astar();
        nextMoves = new LinkedList<>();
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public String getName() {
        return name;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public int dealDamage() {
        return weapon.hit();
    }

    public void takeDamage(int damage) {
        hitpoints -= damage;
    }

    public Position getPosition() {
        return position;
    }

    public void moveEnemy(String dir){
        position.shiftDir(dir);
    }

    public void setStartingPosition(Position position) {
        this.position = position;
    }

    public String getTextureName() {
        return textureName;
    }

    public void runAstar(RoomMap roomMap, MapTile goal) {
        nextMoves = new LinkedList<>();
        MapTile start = roomMap.getMapTile(this.getPosition());
        nextMoves = astar.run(roomMap, start, goal);
    }

    public void moveEnemy(RoomMap roomMap) {
        if (nextMoves.size()>1) {


            if (roomMap.validMove(nextMoves.get(1))) {
                roomMap.getMapTile(this.position).setEmpty();
                position = nextMoves.get(1);
                roomMap.getMapTile(this.position).setOccupiedEntity(this);
            }
            else {
                System.out.println("Eisai malakas");
            }
        }
    }




    @Override
    public String toString() {
        return "Enemy : " + name + " { hitpoints : " + hitpoints + ", weapon : " + weapon + " }";

    }
}
